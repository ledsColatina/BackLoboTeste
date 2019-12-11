package com.LoboProject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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


@RestController
@RequestMapping("/pedido")
public class PedidoService {

	@Autowired
	private PedidoRepository pedidorepository;


	@GetMapping()
	public ResponseEntity<?> BuscarPedido(){
		List<Pedido> x = pedidorepository.findAll();	
		return !x.isEmpty() ? ResponseEntity.ok(x) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> BuscarId(@PathVariable Long id){
		Optional<Pedido> x = pedidorepository.findById(id);	
		return x.isPresent() ? ResponseEntity.ok(x) : ResponseEntity.notFound().build() ;
	}
	
	
	@PostMapping()
	public List<Pedido> criarPedido(@Valid @RequestBody List<Pedido> composicao, HttpServletResponse response) {
		int i = 0;
		List <Pedido> pedidoSalvo = new ArrayList<Pedido>();
		while(composicao.get(i) != null) {
			pedidoSalvo.add( pedidorepository.save(composicao.get(i)));
			i++;
		}
		
		return pedidoSalvo;
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void deletarPedido(@PathVariable Long id){
		pedidorepository.deleteById(id);
	}
	
	
}
