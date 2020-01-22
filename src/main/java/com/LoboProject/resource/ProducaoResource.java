package com.LoboProject.resource;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.LoboProject.domain.Producao;
import com.LoboProject.repository.RegistrarProducaoRepository;
import com.LoboProject.service.ProducaoService;

@RestController
@RequestMapping("/producao")
public class ProducaoResource {

	@Autowired
	private RegistrarProducaoRepository producaoRepository;
	
	@Autowired
	private ProducaoService producaoService;
	
	@GetMapping("/{username}")
	public ResponseEntity<List<Producao>> listarProducao(@PathVariable String username){
		List<Producao> producao = producaoRepository.findAllByOrderByCodigoDesc();
		producao = producaoService.porUser(username);
		producao = producaoService.ordenarProducao(producao);
		return !producao.isEmpty() ? ResponseEntity.ok(producao) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/top50")
	public ResponseEntity<List<Producao>> listar50Producoes(){
		List<Producao> producao = producaoRepository.findTop50ByOrderByCodigoDesc();
		return !producao.isEmpty() ? ResponseEntity.ok(producao) : ResponseEntity.noContent().build();
	}
	
	
	@PostMapping()
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public  ResponseEntity<?> criarProducao(@Valid @RequestBody Producao producao, HttpServletResponse response) {
		return producaoService.criarProducao(producao);
	}
	
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	public ResponseEntity<?> deletarProducao(@PathVariable Long id){
		return producaoService.deletarProducao(id);
	}

}
