package com.LoboProject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.LoboProject.domain.Composicao;
import com.LoboProject.domain.Produto;
import com.LoboProject.repository.ComposicaoRepository;
import com.LoboProject.repository.ProdutoRepository;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ComposicaoRepository composicaoRepository;
	
	@Autowired
	private ComposicaoRepository compRepository;
	
	// Coloca_o_Produto_no_Padrao
	public Produto formatarProduto(Produto produto) {
		String codigo = produto.getCodigo().toLowerCase();
		produto.setCodigo(codigo);
		if(produtoRepository.findBydescricao(produto.getDescricao())!= null) return null;
		if(produto.getSetor().isBase() == true) produto.setComposicao(null);
		return produto;
	}
	
	public Produto verificarCodigo(Produto produto) {
		if(produtoRepository.findById(produto.getCodigo()).isPresent()) return null;
		else return produto;
	}
	
	public ResponseEntity<Produto> decrementosQuantidadeAtual(String id, Long quantidadeAtual) {
		
		Optional<Produto> produto = produtoRepository.findById(id);
		
		return produtoRepository.findById(id)
			    .map(record -> {
			    record.setQuantidadeAtual(produto.get().getQuantidadeAtual() + quantidadeAtual);
			    Produto updated = produtoRepository.save(record);
			    return ResponseEntity.ok().body(updated);
			    }).orElse(ResponseEntity.notFound().build());
	}
	
	public ResponseEntity<?> atualizarProduto(Produto produto, String id) {
		List<String> listaProibida = limitarLoop(id);
		for(int i =0; i < produto.getComposicao().size(); i++) {
			if((listaProibida.contains(produto.getComposicao().get(i).getProdutoParte().getCodigo()))||(listaProibida.indexOf(produto.getComposicao().get(i).getProdutoParte().getCodigo())>= 0)){
				return ResponseEntity.badRequest().body("\n Loop Identificado na Composição !!");
			}
		}
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


	@Transactional
	public void deletarLoteProduto(List<String> produtos) {
		int i = 0;
		while((produtos.get(i) != null)&&(produtos.get(i) != "")) {	
			if(composicaoRepository.findAllByProdutoParte_codigo(produtos.get(i))!= null) composicaoRepository.deleteByprodutoParte_codigo(produtos.get(i));
			produtoRepository.deleteById(produtos.get(i));
			produtos.remove(i);
			i++;
		}
	}
	
	public List<String> limitarLoop(String codigo){
		List<Composicao> lista = compRepository.findAllProdutoTodo(codigo);
		List<Composicao>list2 = new ArrayList<>();
		List<String> codigos = new ArrayList<>();
		int j = 0;
		list2.addAll(lista);
		for(int i = 0; i < lista.size(); i++) {
			if(!codigos.contains(lista.get(i).getProdutoParte().getCodigo())){
				codigos.add(lista.get(i).getProdutoParte().getCodigo());
			}
			if(!codigos.contains(lista.get(i).getId_produto_todo())){
				codigos.add(lista.get(i).getId_produto_todo());
			}
			while((!compRepository.findAllProdutoTodo(list2.get(j).getId_produto_todo()).isEmpty()) ||(j > list2.size())) {
				list2.addAll(compRepository.findAllProdutoTodo(list2.get(j).getId_produto_todo()));
				codigos = limitarLoop2(list2.get(j).getId_produto_todo(), codigos);
				j++;
			}	
			
		}
		return codigos;
	}
	
	public List<String> limitarLoop2(String codigo, List<String> codigos){
		List<Composicao> lista = compRepository.findAllProdutoTodo(codigo);
		for(int i = 0; i < lista.size(); i++) {
			if(!codigos.contains(lista.get(i).getProdutoParte().getCodigo())){
				codigos.add(lista.get(i).getProdutoParte().getCodigo());
			}
			if(!codigos.contains(lista.get(i).getId_produto_todo())){
				codigos.add(lista.get(i).getId_produto_todo());
			}
		}
		return codigos;
	}
	
	
}
