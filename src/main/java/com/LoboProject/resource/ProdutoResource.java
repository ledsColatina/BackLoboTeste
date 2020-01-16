package com.LoboProject.resource;

import java.util.ArrayList;
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
import com.LoboProject.domain.Usuario;
import com.LoboProject.repository.ComposicaoRepository;
import com.LoboProject.repository.ProdutoRepository;
import com.LoboProject.repository.UsuarioRepository;
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
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
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
	
	@GetMapping("/embalagem")
	public ResponseEntity<List<Produto>> buscarProdutoEmbalagem(){
		List<Produto> produtos = produtoRepository.findBySetor_descricao("Embalagem");
		return !produtos.isEmpty() ? ResponseEntity.ok(produtos) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/user/{username}")
	public ResponseEntity<List<Produto>> buscarporUser(@PathVariable String username){
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		List<Produto> list = new ArrayList<>();
		int i;
		if(usuario.isPresent() == false) return null;
		else {
			for(i = 0; i < usuario.get().getSetores().size(); i++) {
				list.addAll( produtoRepository.findBySetor_id(usuario.get().getSetores().get(i).getId()));
			}
			if(usuario.get().isTipo() == true) {
				list = produtoRepository.findAll();
				return !list.isEmpty() ? ResponseEntity.ok(list) : ResponseEntity.noContent().build();
			}
			
		}
		return !list.isEmpty() ? ResponseEntity.ok(list) : ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("/comp/{id}")
	public ResponseEntity<List<Produto>> buscarProdutoPorcomp(@PathVariable String id){
		List<Produto> produtos = produtoRepository.findByComposicao_ProdutoParte_codigo(id);
		return !produtos.isEmpty() ? ResponseEntity.ok(produtos) : ResponseEntity.noContent().build();
	}
	
	
	@PostMapping()
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> criarProduto(@Valid @RequestBody Produto produto, HttpServletResponse response) {
		produto = produtoService.formatarProduto(produto);
		if(produtoService.verificarCodigo(produto) != null) {
			if (produto!=null) return ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produto));
			else return ResponseEntity.badRequest().body("\n Não foi possível Cadastrar, Produto com Descrição Repetida!!");
		}else {
			return ResponseEntity.badRequest().body("\n Não foi possível Cadastrar, Produto com Código Repetido!!");
		}
		
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
