package com.LoboProject.resource;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.LoboProject.Projection.ResumoProduto;
import com.LoboProject.domain.Composicao;
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
	public ResponseEntity<Produto> criarProduto(@Valid @RequestBody Produto produto, HttpServletResponse response) {
		String x = produto.getCodigo().toLowerCase();
		produto.setCodigo(x);
		if(produto.getSetor().isBase() == true) produto.setComposicao(null);
		Produto produtoSalvo = produtoRepository.save(produto);
		return ResponseEntity.status(HttpStatus.OK).body(produtoSalvo);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void deletarProduto(@PathVariable String id){
		composicaoRepository.deleteByprodutoParte_codigo(id);
		produtoRepository.deleteById(id);
	}
	
	@DeleteMapping("/all") 
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void DeletarLoteProdutos(@RequestBody List<String> produtos) {
		int i = 0;
		while((produtos.get(i) != null)&&(produtos.get(i) != "")) {	
			if(composicaoRepository.findAllByProdutoParte_codigo(produtos.get(i))!= null) composicaoRepository.deleteByprodutoParte_codigo(produtos.get(i));
			produtoRepository.deleteById(produtos.get(i));
			produtos.remove(i);
			i++;
		}
		
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<Produto> atualizarProduto(@PathVariable String id,@Valid @RequestBody Produto produto){
		if(produto.getComposicao().contains(produtoRepository.findById(id))) return ResponseEntity.status(HttpStatus.CONFLICT).build();
	
		else if (ProcurarBugs(produto,produto.getComposicao(), id , 0)  == -1) return ResponseEntity.status(HttpStatus.CONFLICT).build();
		
		else if (ProcurarBugs(produto, produto.getComposicao(),id , 0) != -1){
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
		return null;
	}
	
	
	public int CompararBD(Produto produto, String id) {
		int i;
		int j;
		int k;
		List<Produto> x = produtoRepository.findByComposicao_ProdutoParte_codigo(id);
		for(i = 0; i < x.size(); i++) {
			for(j = 0; j < produto.getComposicao().size(); j++) {
	
				if ((x.get(i).getCodigo().equals(produto.getComposicao().get(j).getProdutoParte().getCodigo()) ==  true) ||(x.get(i).getCodigo() == produto.getComposicao().get(j).getProdutoParte().getCodigo())) {
					return 0;
				}
				if((produto.getComposicao().get(j).getProdutoParte().getCodigo().equalsIgnoreCase(x.get(i).getCodigo()) ==  true) ||( produto.getComposicao().get(j).getProdutoParte().getCodigo() == x.get(i).getCodigo() )) {
					return 0;
				}
				
				for(k = 0; k < x.get(i).getComposicao().size(); k++) {
					if((x.get(i).getComposicao().get(k).getCodigo().equals(produto.getComposicao().get(j).getProdutoParte().getCodigo()) ==  true) ||(x.get(i).getComposicao().get(k).getProdutoParte().getCodigo() == produto.getComposicao().get(j).getProdutoParte().getCodigo())) {
						return 0;
					}
				}
			}
		}
		return 1;
	}
	
	public int ProcurarBugs(Produto produto,List<Composicao> lista, String id, int a) {
		List<Composicao> compInic = lista;
		List<Produto> x = produtoRepository.findByComposicao_ProdutoParte_codigo(id);
		int i, j;
		if (a == -1) return -1;
		if((x == null)||(x.isEmpty())) return 0;
		
		for(i = 0; i < x.size(); i++) {
			for(j = 0; j < produto.getComposicao().size(); j++) {
	
				if ((x.contains(compInic.get(j).getProdutoParte()) ==  true)) {
					a = -1;
					return -1;
				}
			}
		}
		
		ProcurarBugs(x.get(a), lista, x.get(a).getCodigo(), a++);
		return -1;
	}
	
	@PutMapping("/{id}/{quantidadeAtual}")
	public ResponseEntity<Produto> registrarProducao(@PathVariable String id,@PathVariable Long quantidadeAtual){
		
		Optional<Produto> x = produtoRepository.findById(id);
		
		return produtoRepository.findById(id)
		    .map(record -> {
		    record.setQuantidadeAtual(x.get().getQuantidadeAtual() + quantidadeAtual);
		    Produto updated = produtoRepository.save(record);
		    return ResponseEntity.ok().body(updated);
		    }).orElse(ResponseEntity.notFound().build());
	}

}
