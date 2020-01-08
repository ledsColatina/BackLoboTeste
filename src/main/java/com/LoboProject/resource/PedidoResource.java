package com.LoboProject.resource;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.LoboProject.repository.PedidoRepository;
import com.LoboProject.service.PedidoService;


@RestController
@RequestMapping("/pedidos")
public class PedidoResource {

	@Autowired
	private PedidoRepository pedidorepository;

	@Autowired
	private PedidoService pedidoService;
	
	
	@GetMapping("/{tipo}")
	public ResponseEntity<List<Pedido>> BuscarPedido(@PathVariable String tipo){
		List<Pedido> lista = pedidoService.listarSeparadamente(tipo);
		return !lista.isEmpty() ? ResponseEntity.ok(lista) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/listar")
	public ResponseEntity<List<Pedido>> listarTudo() {
		List<Pedido> lista = pedidorepository.findAll();	
		return !lista.isEmpty() ? ResponseEntity.ok(lista) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<?> BuscarId(@PathVariable Long id){
		Optional<Pedido> x = pedidorepository.findById(id);	
		return x.isPresent() ? ResponseEntity.ok(x) : ResponseEntity.notFound().build() ;
	}
	
	
	@PostMapping()
	public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
		Pedido pedidoSalvo = pedidorepository.save(pedido);
		return ResponseEntity.ok().body(pedidoSalvo);
	}
	
	@PostMapping("/fila")
	@Transactional
	public ResponseEntity<List<Pedido>> criarFila (@RequestBody List<Pedido> pedidos){
		return ResponseEntity.ok().body(pedidorepository.saveAll(pedidoService.criarFila(pedidos)));
	}
	
	@PostMapping("/Nota")
	@Transactional
	public ResponseEntity<List<Pedido>> gerarNotaFiscal (@RequestBody List<Pedido> pedidos){
		return ResponseEntity.ok().body(pedidorepository.saveAll(pedidoService.gerarNota(pedidos)));
	}
	
	@PostMapping("/Expedicao")
	@Transactional
	public ResponseEntity<List<Pedido>> gerarExpedicao (@RequestBody List<Pedido> pedidos){
		return ResponseEntity.ok().body(pedidorepository.saveAll(pedidoService.expedir(pedidos)));
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void deletarPedido(@PathVariable Long id){
		pedidorepository.deleteById(id);
	}
	
	
}
