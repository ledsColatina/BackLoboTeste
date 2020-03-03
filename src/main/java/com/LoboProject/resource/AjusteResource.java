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
import com.LoboProject.domain.Ajuste;
import com.LoboProject.repository.AjusteRepository;
import com.LoboProject.service.AjusteService;


@RestController
@RequestMapping("/ajustes")
public class AjusteResource {
	
	@Autowired
	private AjusteRepository ajusteRepository;
	
	@Autowired
	private AjusteService ajusteService;
	
	
	@GetMapping
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<List<Ajuste>> listarAjustes(){
		List<Ajuste> ajustes = ajusteRepository.findAllByOrderByCodigoDesc();
		return !ajustes.isEmpty() ? ResponseEntity.ok(ajustes) : ResponseEntity.noContent().build();
	}
	
	
	@PostMapping()
	@PreAuthorize("hasAuthority('USER')")
	public  ResponseEntity<?> criarAjustes(@Valid @RequestBody Ajuste ajuste, HttpServletResponse response) {
		return ajusteService.criarAjuste(ajuste);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> deletarAjuste(@PathVariable Long id){
		return ajusteService.deletarAjuste(id);
	}
	
}
