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
import com.LoboProject.domain.Producao;
import com.LoboProject.domain.Produto;
import com.LoboProject.repository.ProdutoRepository;
import com.LoboProject.repository.RegistrarProducaoRepository;

@RestController
@RequestMapping("/producao")
public class ProducaoResource {

	@Autowired
	private RegistrarProducaoRepository producaoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping
	public ResponseEntity<?> listarSetor(){
		List<Producao> producao = producaoRepository.findAll();
		return !producao.isEmpty() ? ResponseEntity.ok(producao) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/top50")
	public ResponseEntity<?> listarSetor2(){
		List<Producao> producao = producaoRepository.findTop50ByOrderByCodigoDesc();
		return !producao.isEmpty() ? ResponseEntity.ok(producao) : ResponseEntity.noContent().build();
	}
	
	
	@PostMapping()
	public  ResponseEntity<?> criarProduto(@Valid @RequestBody Producao producao, HttpServletResponse response) {
		producao.setData(new java.util.Date(System.currentTimeMillis()));
		Optional<Produto> x = produtoRepository.findById(producao.getProduto().getCodigo());
		x.get().setQuantidadeAtual(x.get().getQuantidadeAtual() + producao.getQuantidade());
		producao.setProduto(x.get());
		
		if(verificarComp(x.get(), producao) == 1) {
		int i = 0;
		if(!x.get().getComposicao().isEmpty()) {
			for(i = 0; i < x.get().getComposicao().size(); i++) {
				if(!x.get().getComposicao().isEmpty()) {
					x = decrementarComposicao(x,producao, i);
				}
			}
		}
			produtoRepository.save(x.get());
			// Salvando_producao
			Producao producaoSalva = producaoRepository.save(producao);
			return ResponseEntity.status(HttpStatus.OK).body(producaoSalva);
		}
		else return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}
	
	
	public int verificarComp(Produto produto, Producao prod) {
		int i = 0;
		if(!produto.getComposicao().isEmpty()) {
			for(i = 0; i < produto.getComposicao().size(); i++) {
				if((produto.getComposicao().get(i).getProdutoParte().getQuantidadeAtual() < (produto.getComposicao().get(i).getQuantidade() * prod.getQuantidade()))){
					return 0;
				}
			}
		}
		return 1;
		
	}
	
	
	public Optional<Produto> decrementarComposicao(Optional<Produto> x, Producao prod, int i) {
		Long valor;
		
		if(x.get() != null) {
			valor = (x.get().getComposicao().get(i).getProdutoParte().getQuantidadeAtual() - (x.get().getComposicao().get(i).getQuantidade() * prod.getQuantidade()));
			x.get().getComposicao().get(i).getProdutoParte().setQuantidadeAtual(valor);
			produtoRepository.save(x.get().getComposicao().get(i).getProdutoParte());
		}
		return x;
	}
	
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarProduto(@PathVariable Long id){
		Optional<Producao> prod = producaoRepository.findById(id);
		Optional<Produto> x = produtoRepository.findById(prod.get().getProduto().getCodigo());
		x.get().setQuantidadeAtual(x.get().getQuantidadeAtual() - prod.get().getQuantidade());
		produtoRepository.save(x.get());
		producaoRepository.deleteById(id);
	}

}
