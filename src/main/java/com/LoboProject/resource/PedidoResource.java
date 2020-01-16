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
import com.LoboProject.domain.Pedido;
import com.LoboProject.domain.PedidoProduto;
import com.LoboProject.domain.SimpleEnum;
import com.LoboProject.repository.PedidoProdutoRepository;
import com.LoboProject.repository.PedidoRepository;
import com.LoboProject.service.PedidoService;


@RestController
@RequestMapping("/pedidos")
public class PedidoResource {

	@Autowired
	private PedidoRepository pedidorepository;

	@Autowired
	private PedidoProdutoRepository pedidoProdutorepository;
	
	@Autowired
	private PedidoService pedidoService;
	
	
	@GetMapping("/{tipo}")
//	@PreAuthorize("hasAuthority('USER')")
//	@PreAuthorize("hasAuthority('EMBALAGEM') OR ('ADMIN')")
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
		return !lista.isEmpty() ? ResponseEntity.ok(lista) : ResponseEntity.notFound().build() ;
	}
	
	@GetMapping("/demandasProd/{username}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<List<PedidoProduto>> buscarDemandasProduto(@PathVariable String username){
		List<PedidoProduto> lista = pedidoService.filtroPorUser(username);
		lista = pedidoService.atualizarQtdP(lista);
		return ResponseEntity.ok().body(lista);
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
	public ResponseEntity<String> criarFila (@RequestBody List<Pedido> pedidos){
		//List<Pedido> x = 
		//if (x.isEmpty()) ResponseEntity.badRequest().body("Fail");
		//pedidorepository.saveAll(x);
		return pedidoService.criarFila(pedidos) != null ? ResponseEntity.ok().body("aaa") :  ResponseEntity.badRequest().body("aaa");
	}
	
	@PostMapping("/embalar")
	@PreAuthorize("hasAuthority('ADMIN')")
	@Transactional
	public ResponseEntity<Pedido> Embalar (@RequestBody Pedido pedido){
		return ResponseEntity.ok().body(pedidorepository.save(pedidoService.embalar(pedido)));
	}
	
	@PostMapping("/nota")
	@PreAuthorize("hasAuthority('ADMIN')")
	@Transactional
	public ResponseEntity<Pedido> gerarNotaFiscal (@RequestBody Pedido pedido){
		return ResponseEntity.ok().body(pedidorepository.save(pedidoService.gerarNota(pedido)));
	}
	
	@PostMapping("/expedicao")
	@PreAuthorize("hasAuthority('EMBALAGEM') AND ('ADMIN')")
	@Transactional
	public ResponseEntity<Pedido> gerarExpedicao (@RequestBody Pedido pedido){
		return ResponseEntity.ok().body(pedidorepository.save(pedidoService.expedir(pedido)));
	}
	
	@PostMapping("/embalagem/{codigoPedido}/{codigo}/{quantidade}")
//	@PreAuthorize("hasAuthority('EMBALAGEM') AND ('ADMIN')")
	@Transactional
	public ResponseEntity<String> embalarPedidos(@PathVariable long codigoPedido, @PathVariable String codigo, @PathVariable int quantidade){
		if(pedidoService.DiminuirEmbalagem(codigoPedido,codigo, quantidade) == "Ok") {
			pedidoProdutorepository.save(pedidoService.minimizarMovimentoChave(codigoPedido, codigo, quantidade));
		}
		return ResponseEntity.ok().body(pedidoService.DiminuirEmbalagem(codigoPedido,codigo, quantidade));
	}
	
	@PostMapping("/embalar/{codigoPedido}")
//	@PreAuthorize("hasAuthority('EMBALAGEM') OR ('ADMIN')")
	@Transactional
	public ResponseEntity<Pedido> trocarEstadoEmbalagem(@PathVariable long codigoPedido){
		Optional<Pedido> pedido = pedidorepository.findById(codigoPedido);
		pedido.get().setStatus(SimpleEnum.Status.EMBALADO);
		pedidoService.DiminuirEmbalagem2(codigoPedido);
		return ResponseEntity.ok().body(pedidorepository.save(pedido.get()));
	}
	
	
	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority ('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarPedido(@PathVariable Long codigo){
		pedidoService.deletar(codigo);
	}
	
}
