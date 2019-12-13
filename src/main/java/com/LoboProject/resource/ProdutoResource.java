package com.LoboProject.resource;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.LoboProject.Projection.ResumoProduto;
import com.LoboProject.domain.Produto;
import com.LoboProject.repository.ComposicaoRepository;
import com.LoboProject.repository.ProdutoRepository;
import com.LoboProject.service.ProdutoService;


@RestController
@RequestMapping("/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ComposicaoRepository composicaoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	
	@GetMapping
	public ResponseEntity<List<Produto>> listarProduto(){
		List<Produto> produto = produtoRepository.findAll();
		return !produto.isEmpty() ? ResponseEntity.ok(produto) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/resumo")
	public ResponseEntity<List<ResumoProduto>> resumir(){
		List<ResumoProduto> produto = produtoRepository.resumir();
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
	
	@GetMapping("/comp/{id}")
	public ResponseEntity<List<Produto>> buscarProdutoPorcomp(@PathVariable String id){
		List<Produto> produtos = produtoRepository.findByComposicao_ProdutoParte_codigo(id);
		return !produtos.isEmpty() ? ResponseEntity.ok(produtos) : ResponseEntity.noContent().build();
	}
	
	@PostMapping()
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Produto> criarProduto(@Valid @RequestBody Produto produto, HttpServletResponse response) {
		produto = produtoService.formatarProduto(produto);
		produtoRepository.save(produto);
		return ResponseEntity.status(HttpStatus.OK).body(produto);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deletarProduto(@PathVariable String id){
		composicaoRepository.deleteByprodutoParte_codigo(id);
		produtoRepository.deleteById(id);
	}
	
	@DeleteMapping("/all") 
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ADMIN')")
	public void DeletarLoteProdutos(@RequestBody List<String> produtos) {
		produtoService.deletarLoteProduto(produtos);
	}
	

	@PutMapping("/{id}")
	@Transactional
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> atualizarProduto(@PathVariable String id,@Valid @RequestBody Produto produto){
		return produtoService.atualizarProduto(produto, id);
	}
	
	
	@PutMapping("/{id}/{quantidadeAtual}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Produto> registrarProducao(@PathVariable String id,@PathVariable Long quantidadeAtual){
		return produtoService.decrementosQuantidadeAtual(id, quantidadeAtual);
	}

}
