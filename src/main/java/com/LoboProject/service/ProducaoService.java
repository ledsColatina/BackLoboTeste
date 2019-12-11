package com.LoboProject.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.LoboProject.domain.Producao;
import com.LoboProject.domain.Produto;
import com.LoboProject.repository.ProdutoRepository;
import com.LoboProject.repository.RegistrarProducaoRepository;

@Service
public class ProducaoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private RegistrarProducaoRepository producaoRepository;
	
	
	public ResponseEntity<?> deletarProducao(Long id){
		Optional<Producao> prod = producaoRepository.findById(id);
		Optional<Produto> produto = produtoRepository.findById(prod.get().getProduto().getCodigo());
		if(produto.get().getQuantidadeAtual()- prod.get().getQuantidade() < 0) return ResponseEntity.ok("BLOQUEADO: Produção Já Efetuada de Outro Produto");
		produto.get().setQuantidadeAtual(produto.get().getQuantidadeAtual() - prod.get().getQuantidade());
		produtoRepository.save(produto.get());
		producaoRepository.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
	
	
	public Optional<Produto> decrementarComposicao(Optional<Produto> produto, Producao prod, int i) {
		Long valor;
		
		if(produto.get() != null) {
			valor = (produto.get().getComposicao().get(i).getProdutoParte().getQuantidadeAtual() - (produto.get().getComposicao().get(i).getQuantidade() * prod.getQuantidade()));
			if(valor < 0) return null;
			produto.get().getComposicao().get(i).getProdutoParte().setQuantidadeAtual(valor);
			produtoRepository.save(produto.get().getComposicao().get(i).getProdutoParte());
		}
		return produto;
	}
	
	
	public  ResponseEntity<?> criarProducao(Producao producao) {
		producao.setData(new java.util.Date(System.currentTimeMillis()));
		Optional<Produto> produto = produtoRepository.findById(producao.getProduto().getCodigo());
		produto.get().setQuantidadeAtual(produto.get().getQuantidadeAtual() + producao.getQuantidade());
		producao.setProduto(produto.get());
		
		if(verificarComp(produto.get(), producao) == 1) {
		int i = 0;
		if(!produto.get().getComposicao().isEmpty()) {
			for(i = 0; i < produto.get().getComposicao().size(); i++) {
				if(!produto.get().getComposicao().isEmpty()) {
					produto = decrementarComposicao(produto,producao, i);
				}
			}
		}
			produtoRepository.save(produto.get());
			// Salvando_producao
			Producao producaoSalva = producaoRepository.save(producao);
			return ResponseEntity.status(HttpStatus.OK).body(producaoSalva);
		}
		else return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}
	
}
