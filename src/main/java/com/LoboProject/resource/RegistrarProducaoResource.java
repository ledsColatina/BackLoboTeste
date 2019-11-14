package com.LoboProject.resource;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.LoboProject.domain.Produto;
import com.LoboProject.domain.RegistrarProducao;
import com.LoboProject.repository.ProdutoRepository;
import com.LoboProject.repository.RegistrarProducaoRepository;

@RestController
@RequestMapping("/producao")
public class RegistrarProducaoResource {

	@Autowired
	private RegistrarProducaoRepository producaoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping
	public ResponseEntity<?> listarSetor(){
		List<RegistrarProducao> producao = producaoRepository.findAll();
		return !producao.isEmpty() ? ResponseEntity.ok(producao) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/top10")
	public ResponseEntity<?> listarSetor2(){
		List<RegistrarProducao> producao = producaoRepository.findTop10ByOrderByCodigoDesc();
		return !producao.isEmpty() ? ResponseEntity.ok(producao) : ResponseEntity.noContent().build();
	}
	
	
	@PostMapping()
	public ResponseEntity<RegistrarProducao> criarProduto(@Valid @RequestBody RegistrarProducao producao, HttpServletResponse response) {
		producao.setData(new java.util.Date(System.currentTimeMillis()));
		Optional<Produto> x = produtoRepository.findById(producao.getProduto().getCodigo());
		x.get().setQuantidadeAtual(x.get().getQuantidadeAtual() + producao.getQuantidade());
		producao.setProduto(x.get());
		produtoRepository.save(x.get());
		RegistrarProducao producaoSalva = producaoRepository.save(producao);
		return ResponseEntity.status(HttpStatus.OK).body(producaoSalva);
	}
	
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarProduto(@PathVariable Long id){
		producaoRepository.deleteById(id);
	}

}
