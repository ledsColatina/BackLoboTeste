package com.LoboProject.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.LoboProject.Projection.ResumoComposicao;
import com.LoboProject.domain.Composicao;
import com.LoboProject.repository.ComposicaoRepository;


@RestController
@RequestMapping("/composicao")
public class ComposicaoResource {

	@Autowired
	private ComposicaoRepository composicaorepository;
	
	@GetMapping
	public ResponseEntity<List<Composicao>> listarComposicao(){
		List<Composicao> composicao = composicaorepository.findAll();
		return !composicao.isEmpty() ? ResponseEntity.ok(composicao) : ResponseEntity.noContent().build();
	}
	

	@GetMapping("/resumo")
	public ResponseEntity<List<ResumoComposicao>> resumo(){
		List<ResumoComposicao> composicao = composicaorepository.resumir();
		return !composicao.isEmpty() ? ResponseEntity.ok(composicao) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<?> listarComposicaoCod(@PathVariable Long codigo){
		Optional<Composicao> composicao = composicaorepository.findById(codigo);
		return composicao.isPresent() ? ResponseEntity.ok(composicao) : ResponseEntity.notFound().build() ;
	}
	
	
	@PostMapping()
	public List<Composicao> criarComposicao(@Valid @RequestBody List<Composicao> composicao, HttpServletResponse response) {
		int i = 0;
		List <Composicao>composicaoSalvo = new ArrayList<Composicao>();
		while(composicao.get(i) != null) {
			composicaoSalvo.add( composicaorepository.save(composicao.get(i)));
			i++;
		}
		return composicaoSalvo;
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarComposicao(@PathVariable Long codigo){
		composicaorepository.deleteById(codigo);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<Composicao> atualizarSetor(@PathVariable Long codigo, @Valid @RequestBody Composicao composicao){
		return composicaorepository.findById(codigo)
		           .map(record -> {
		               record.setProdutoParte(composicao.getProdutoParte());
		               record.setQuantidade(composicao.getQuantidade());
		               Composicao updated = composicaorepository.save(record);
		               return ResponseEntity.ok().body(updated);
		           }).orElse(ResponseEntity.notFound().build());
	}
	
}
