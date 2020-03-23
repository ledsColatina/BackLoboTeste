package com.LoboProject.resource;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.LoboProject.domain.Composicao;
import com.LoboProject.domain.Pedido;
import com.LoboProject.domain.PedidoProduto;
import com.LoboProject.domain.SimpleEnum;
import com.LoboProject.repository.ComposicaoRepository;
import com.LoboProject.repository.PedidoProdutoRepository;
import com.LoboProject.repository.PedidoRepository;
import com.LoboProject.service.PedidoService;
import com.LoboProject.service.ProdutoService;


@RestController
@RequestMapping("/pedidos")
public class PedidoResource {

	@Autowired
	private PedidoRepository pedidorepository;

	@Autowired
	private PedidoProdutoRepository pedidoProdutorepository;
	
	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private ComposicaoRepository comprepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping("/{tipo}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERÊNCIA DE PRODUÇÃO', 'USER')")
	public ResponseEntity<List<Pedido>> BuscarPedido(@PathVariable String tipo){
		List<Pedido> lista = pedidoService.listarSeparadamente(tipo);
		lista = pedidoService.contagemVolumes(lista);
		return !lista.isEmpty() ? ResponseEntity.ok(lista) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{tipo}/notas")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<List<Pedido>> BuscarPedidoNota(@PathVariable String tipo){
		List<Pedido> lista = pedidoService.listarSeparadamente(tipo);
		lista = pedidoService.contagemVolumes(lista);
		lista = pedidoService.modificarParaEmbalagens(lista);
		return !lista.isEmpty() ? ResponseEntity.ok(lista) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/demandas/{username}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<List<Pedido>> buscarDemandas(@PathVariable String username){
		return pedidoService.buscarDemandas(username);
	}
	
	@GetMapping("/demandasProd/{username}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<List<PedidoProduto>> buscarDemandasProduto(@PathVariable String username){
		return pedidoService.buscarDemandasProduto(username);
	}
	
	@GetMapping("/estoque/{id_produto_parte}")
	public List<String> estoque(@PathVariable String id_produto_parte){
		return produtoService.limitarLoop(id_produto_parte);
	}
	
	@GetMapping("/estoqus/{id_produto_parte}")
	public List<Composicao> estoque2(@PathVariable String id_produto_parte){
		return comprepository.findAllProdutoTodo(id_produto_parte);
	}
	
	@PostMapping
	public ResponseEntity<?> criarPedido(@RequestBody Pedido pedido) {
		if (pedidorepository.findById(pedido.getCodigo()).isPresent()) return ResponseEntity.badRequest().body("\n Pedido com Código repetido!");
		List<PedidoProduto> lista = pedidoService.itensPedido(pedido);
		//if (lista.isEmpty()) return ResponseEntity.badRequest().body("\n Faltam Itens no Pedido ou alguns Itens não foram reconhecidos!");
		pedido.setItens(null);
		Pedido pedidoSalvo = pedidorepository.save(pedido);
		for(int i = 0; i < lista.size(); i++) pedidoProdutorepository.save(lista.get(i));
		return ResponseEntity.ok().body(pedidoSalvo);
	}
	
	
	@PostMapping("/fila")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERÊNCIA DE PRODUÇÃO')")
	public ResponseEntity<List<Pedido>> criarFila (@RequestBody List<Pedido> pedidos){
		return pedidoService.criarFila(pedidos) != null ? ResponseEntity.ok().body(pedidos) :  ResponseEntity.badRequest().body(null);
	}
	
	@PostMapping("/embalar")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'EMBALAGEM')")
	@Transactional
	public ResponseEntity<Pedido> Embalar (@RequestBody Pedido pedido){
		return ResponseEntity.ok().body(pedidorepository.save(pedidoService.embalar(pedido)));
	}
	
	@PostMapping("/nota")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'NOTAS FISCAIS')")
	@Transactional
	public ResponseEntity<Pedido> gerarNotaFiscal (@RequestBody Pedido pedido){
		return ResponseEntity.ok().body(pedidorepository.save(pedidoService.gerarNota(pedido)));
	}
	
	@PostMapping("/expedicao")
	@PreAuthorize("hasAnyAuthority('ADMIN','EXPEDIÇÃO')")
	@Transactional
	public ResponseEntity<Pedido> gerarExpedicao (@RequestBody Pedido pedido){
		return ResponseEntity.ok().body(pedidorepository.save(pedidoService.expedir(pedido)));
	}
	
	@PostMapping("/embalagem/{codigoPedido}/{codigo}/{quantidade}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'EMBALAGEM')") 
	public ResponseEntity<String> embalarPedidos(@PathVariable long codigoPedido, @PathVariable String codigo, @PathVariable int quantidade){
		if(pedidoService.DiminuirEmbalagem(codigoPedido,codigo, quantidade).equals("OK")) {
			PedidoProduto item = pedidoService.minimizarMovimentoChave(codigoPedido, codigo, quantidade);
			pedidoProdutorepository.save(item);
			return ResponseEntity.ok().body("Ok");
		}else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estoque Insuficiente para o Pedido!!");
	}
	
	@PostMapping("/embalar/{codigoPedido}")
	@PreAuthorize("hasAnyAuthority('EMBALAGEM', 'ADMIN')")
	@Transactional
	public ResponseEntity<Pedido> trocarEstadoEmbalagem(@PathVariable long codigoPedido){
		Optional<Pedido> pedido = pedidorepository.findById(codigoPedido);
		pedido.get().setStatus(SimpleEnum.Status.EMBALADO);
		pedidoService.DiminuirEmbalagem2(codigoPedido);
		return ResponseEntity.ok().body(pedidorepository.save(pedido.get()));
	}
	
	
	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAnyAuthority ('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarPedido(@PathVariable Long codigo){
		pedidoService.deletar(codigo);
	}
	
}
