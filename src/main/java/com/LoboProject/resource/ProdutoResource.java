package com.LoboProject.resource;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.LoboProject.domain.Produto;
import com.LoboProject.repository.ComposicaoRepository;
import com.LoboProject.repository.ProdutoRepository;



@RestController
@RequestMapping("/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ComposicaoRepository composicaoRepository;
	
	
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
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarProduto(@PathVariable String id){
		composicaoRepository.deleteByprodutoParte_codigo(id);
		produtoRepository.deleteById(id);
	}
	
	@DeleteMapping("/all")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void DeletarLoteProdutos(@RequestBody List<String> produtos) {
		int i = 0;
		while((produtos.get(i) != null)&&(produtos.get(i) != "")) {	
			if(composicaoRepository.findAllByProdutoParte_codigo(produtos.get(i))!= null) composicaoRepository.deleteByprodutoParte_codigo(produtos.get(i));
			produtoRepository.deleteById(produtos.get(i));
			produtos.remove(i);
			i++;
		}
		
	}
	
	@PutMapping("/{id}")
	@SuppressWarnings("unlikely-arg-type")
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
