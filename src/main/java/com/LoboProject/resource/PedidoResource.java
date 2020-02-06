package com.LoboProject.resource;

import java.util.ArrayList;
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
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<List<Pedido>> BuscarPedido(@PathVariable String tipo){
		List<Pedido> lista = pedidoService.listarSeparadamente(tipo);
		return !lista.isEmpty() ? ResponseEntity.ok(lista) : ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("/demandas/{username}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<List<Pedido>> buscarDemandas(@PathVariable String username){
		List <Pedido> lista = pedidoService.listarSeparadamente("EM_PRODUCAO");
		for(int i = 0; i < lista.size(); i++) {
			lista.get(i).setItens(pedidoService.filtroPorUserSetor(username, lista.get(i).getCodigo()));
		}
		lista.addAll(pedidoService.estoqueMin());
		lista = pedidoService.formatarTirandoRepetidos(lista);
		lista = pedidoService.quebrarDemandas(lista);
		return !lista.isEmpty() ? ResponseEntity.ok(lista) : ResponseEntity.notFound().build() ;
	}
	
	@GetMapping("/demandasProd/{username}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<List<PedidoProduto>> buscarDemandasProduto(@PathVariable String username){
		List<PedidoProduto> lista = new ArrayList<PedidoProduto>();// = pedidoService.filtroPorUser(username);
		List<Pedido> aux = buscarDemandas(username).getBody();
		for(int i = 0; i < aux.size(); i++)  lista.addAll(pedidoService.atualizarQtdP(aux.get(i).getItens()));
		lista = pedidoService.formatarComposicaoSemSomar(lista);
		return ResponseEntity.ok().body(lista);
	}
	
	@GetMapping("/estoque/{id_produto_parte}")
	public List<String> estoque(@PathVariable String id_produto_parte){
		return produtoService.limitarLoop(id_produto_parte);
	}
	
	@GetMapping("/estoqus/{id_produto_parte}")
	public List<Composicao> estoque2(@PathVariable String id_produto_parte){
		return comprepository.findAllProdutoTodo(id_produto_parte);
	}
	
	@PostMapping()
	public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
		List<PedidoProduto> lista = pedidoService.itensPedido(pedido);
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
	@PreAuthorize("hasAnyAuthority ('ADMIN', 'USER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarPedido(@PathVariable Long codigo){
		pedidoService.deletar(codigo);
	}
	
}
