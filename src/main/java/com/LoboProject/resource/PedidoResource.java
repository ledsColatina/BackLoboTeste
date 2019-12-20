package com.LoboProject.resource;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
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
import com.LoboProject.repository.PedidoProdutoRepository;
import com.LoboProject.repository.PedidoRepository;
import com.LoboProject.service.PedidoService;


@RestController
@RequestMapping("/pedido")
public class PedidoResource {

	@Autowired
	private PedidoRepository pedidorepository;

	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private PedidoProdutoRepository pedidoProduto;
	
	@GetMapping()
	public ResponseEntity<?> BuscarPedido(){
		List<Pedido> x = pedidorepository.findAll();	
		return !x.isEmpty() ? ResponseEntity.ok(x) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/ler")
	public void ler() {
		pedidoService.lerXML();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> BuscarId(@PathVariable Long id){
		Optional<Pedido> x = pedidorepository.findById(id);	
		return x.isPresent() ? ResponseEntity.ok(x) : ResponseEntity.notFound().build() ;
	}
	
	
	@PostMapping()
	public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido, HttpServletResponse response) {
		int i = 0;
		for(i = 0; i < pedido.getItens().size(); i++)pedidoProduto.save((pedido.getItens().get(i)));
		Pedido pedidoSalvo = pedidorepository.save(pedido);
		return ResponseEntity.ok().body(pedidoSalvo);
	}
	
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void deletarPedido(@PathVariable Long id){
		pedidorepository.deleteById(id);
	}
	
	
}
