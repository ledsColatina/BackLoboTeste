package com.LoboProject.resource;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.LoboProject.domain.Composicao;
import com.LoboProject.domain.Produto;
import com.LoboProject.repository.ProdutoRepository;



@RestController
@RequestMapping("/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	
	@GetMapping
	public ResponseEntity<List<Produto>> listarProduto(){
		List<Produto> produto = produtoRepository.findAll();
		return !produto.isEmpty() ? ResponseEntity.ok(produto) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarProdutoId(@PathVariable String id){
		Optional<Produto> produto = produtoRepository.findById(id);
		return produto.isPresent() ? ResponseEntity.ok(produto) : ResponseEntity.notFound().build() ;
	}
	
	@GetMapping("/setor/{id}")
	public ResponseEntity<List<Produto>> buscarProdutoPorSetor(@PathVariable Long id){
		List<Produto> produtos = produtoRepository.findBySetor_id(id);
		return !produtos.isEmpty() ? ResponseEntity.ok(produtos) : ResponseEntity.noContent().build();
	}
	
	
	@PostMapping()
	public ResponseEntity<Produto> criarProduto(@Valid @RequestBody Produto produto, HttpServletResponse response) {
		String x = produto.getCodigo().toLowerCase();
		produto.setCodigo(x);
		if(produto.getSetor().isBase() == true) produto.setComposicao(null);
		Produto produtoSalvo = produtoRepository.save(produto);
		return ResponseEntity.status(HttpStatus.OK).body(produtoSalvo);
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarProduto(@PathVariable String codigo){
		produtoRepository.deleteById(codigo);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Produto> atualizarSetor(@PathVariable String id,@Valid @RequestBody Produto produto){
		
		if(produto.getComposicao().contains(produtoRepository.findById(id))) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		
		else{
			return produtoRepository.findById(id)
		
		           .map(record -> {
		               record.setDescricao(produto.getDescricao());
		               record.setQuantidadeAtual(produto.getQuantidadeAtual());
		               record.setQuantidadeMin(produto.getQuantidadeMin());
		               record.setQuantidadeMax(produto.getQuantidadeMax());
		               record.setSetor(produto.getSetor());
		               record.setComposicao(produto.getComposicao());
		               Produto updated = produtoRepository.save(record);
		               return ResponseEntity.ok().body(updated);
		           }).orElse(ResponseEntity.notFound().build());
		}
	}
	
}
