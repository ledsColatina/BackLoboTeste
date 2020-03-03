package com.LoboProject.resource;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.LoboProject.Projection.ResumoComposicao;
import com.LoboProject.domain.Composicao;
import com.LoboProject.repository.ComposicaoRepository;
import com.LoboProject.service.ComposicaoService;


@RestController
@RequestMapping("/composicao")
public class ComposicaoResource {

	@Autowired
	private ComposicaoRepository composicaoRepository;
	
	@Autowired
	private ComposicaoService composicaoService;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<Composicao>> listarComposicao(){
		List<Composicao> composicao = composicaoRepository.findAll();
		return !composicao.isEmpty() ? ResponseEntity.ok(composicao) : ResponseEntity.noContent().build();
	}
	

	@GetMapping("/resumo")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<ResumoComposicao>> resumoComposicao(){
		List<ResumoComposicao> composicao = composicaoRepository.resumir();
		return !composicao.isEmpty() ? ResponseEntity.ok(composicao) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> listarComposicaoCod(@PathVariable Long codigo){
		Optional<Composicao> composicao = composicaoRepository.findById(codigo);
		return composicao.isPresent() ? ResponseEntity.ok(composicao) : ResponseEntity.notFound().build() ;
	}
	
	
	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<Composicao> criarComposicao(@Valid @RequestBody List<Composicao> composicao, HttpServletResponse response) {
		return composicaoService.criarComposicao(composicao);
	}
	
	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Composicao> atualizarSetor(@PathVariable Long codigo, @Valid @RequestBody Composicao composicao){
		return composicaoService.atualizarComposicao(codigo, composicao);
	}
	
	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarComposicao(@PathVariable Long codigo){
		composicaoRepository.deleteById(codigo);
	}
	
}
