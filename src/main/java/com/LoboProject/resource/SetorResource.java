package com.LoboProject.resource;

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
import com.LoboProject.domain.Setor;
import com.LoboProject.repository.SetorRepository;
import com.LoboProject.service.SetorService;


@RestController
@RequestMapping("/setores")
public class SetorResource {
	
	@Autowired
	private SetorRepository setorRepository;
	
	@Autowired
	private SetorService setorService;
	
	@GetMapping
	public ResponseEntity<List<Setor>> listarSetor(){
		List<Setor> setor = setorRepository.findAll();
		return !setor.isEmpty() ? ResponseEntity.ok(setor) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Setor> BuscarId(@PathVariable Long id){
		Optional<Setor> setor = setorRepository.findById(id);	
		return setor.isPresent() ? ResponseEntity.ok(setor.get()) : ResponseEntity.notFound().build() ;
	}
	
	
	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> CriarSetor(@Valid @RequestBody Setor setor, HttpServletResponse response) {
		Setor setorSalvo = setorRepository.findBydescricao(setor.getDescricao());
		return !setorSalvo.equals(null) ? ResponseEntity.ok().body(setorSalvo) : ResponseEntity.badRequest().body("\n Não foi possível Cadastrar, Setor com Descrição Repetida!!");
	}

	
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Setor> atualizarSetor(@PathVariable Long id, @Valid @RequestBody Setor setor){
		Setor setorup = setorService.atualizar(id, setor);
		return !setorup.equals(null) ? ResponseEntity.ok().body(setorup) : ResponseEntity.notFound().build();
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ADMIN')")
	public void DeletarSetor(@PathVariable Long id) {
		setorRepository.deleteById(id);
	}
	
}
