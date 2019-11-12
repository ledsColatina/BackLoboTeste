package com.LoboProject.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.LoboProject.domain.Setor;
import com.LoboProject.repository.SetorRepository;


@RestController
@RequestMapping("/setores")
public class SetorResource {
	
	@Autowired
	private SetorRepository setorRepository;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_LISTARSETOR')")
	public ResponseEntity<?> listarSetor(){
		List<Setor> setor = setorRepository.findAll();
		return !setor.isEmpty() ? ResponseEntity.ok(setor) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> BuscarId(@PathVariable Long id){
		Optional<Setor> x = setorRepository.findById(id);	
		return x.isPresent() ? ResponseEntity.ok(x) : ResponseEntity.notFound().build() ;
	}
	
	
	@PostMapping()
	//@PreAuthorize("hasAuthority('ROLE_CRIARSETOR')")
	public ResponseEntity<Setor> CriarSetor(@Valid @RequestBody Setor setor, HttpServletResponse response) {
		Setor setorSalvo = setorRepository.save(setor);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
				.buildAndExpand(setorSalvo.getId()).toUri();
		response.setHeader("Location", uri.toASCIIString());
		
		return ResponseEntity.created(uri).body(setorSalvo);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void DeletarSetor(@PathVariable Long id) {
		setorRepository.deleteById(id);
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<Setor> atualizarSetor(@PathVariable Long id, @Valid @RequestBody Setor setor){
		return setorRepository.findById(id)
		           .map(record -> {
		               record.setDescricao(setor.getDescricao());
		               record.setBase(setor.isBase());
		               Setor updated = setorRepository.save(record);
		               return ResponseEntity.ok().body(updated);
		           }).orElse(ResponseEntity.notFound().build());
	}
	
}
