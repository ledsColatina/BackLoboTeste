package com.LoboProject.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.LoboProject.domain.Ajuste;
import com.LoboProject.domain.Produto;
import com.LoboProject.domain.SimpleEnumStat;
import com.LoboProject.domain.Usuario;
import com.LoboProject.repository.AjusteRepository;
import com.LoboProject.repository.ProdutoRepository;
import com.LoboProject.repository.UsuarioRepository;

@Service
public class AjusteService {

	
	@Autowired
	private AjusteRepository ajusteRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private UsuarioRepository userRepository;
	
	public ResponseEntity<?> deletarAjuste(Long id){
		Optional<Ajuste> ajuste = ajusteRepository.findById(id);
		Optional<Produto> produto = produtoRepository.findById(ajuste.get().getProduto().getCodigo());
		
		if((produto.get().getQuantidadeAtual()- ajuste.get().getQuantidade() < 0)&&(SimpleEnumStat.enumInIf(ajuste.get().getTipo()) == "INCREMENTO") ||(SimpleEnumStat.enumInIf(ajuste.get().getTipo()) == "AJUSTE")) return ResponseEntity.ok("BLOQUEADO: AJUSTE NÃO PODE FICAR NEGATIVO");
		
		if(SimpleEnumStat.enumInIf(ajuste.get().getTipo()) == "INCREMENTO") produto.get().setQuantidadeAtual(produto.get().getQuantidadeAtual() + ajuste.get().getQuantidade());
		else if(SimpleEnumStat.enumInIf(ajuste.get().getTipo()) == "DECREMENTO") produto.get().setQuantidadeAtual(produto.get().getQuantidadeAtual() + ajuste.get().getQuantidade());
		else if(SimpleEnumStat.enumInIf(ajuste.get().getTipo()) == "AJUSTE") produto.get().setQuantidadeAtual(produto.get().getQuantidadeAtual() - ajuste.get().getQuantidade());
		produto.get().setQuantidadeAtual(produto.get().getQuantidadeAtual() - ajuste.get().getQuantidade());
		produtoRepository.save(produto.get());
		ajusteRepository.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	public ResponseEntity<?> criarAjuste(Ajuste ajuste) {
		ajuste.setData(new java.util.Date(System.currentTimeMillis()));
		Optional<Produto> produto = produtoRepository.findById(ajuste.getProduto().getCodigo());
		if((produto.get().getQuantidadeAtual()- ajuste.getQuantidade() < 0)&&(SimpleEnumStat.enumInIf(ajuste.getTipo()) == "DECREMENTO")) return ResponseEntity.ok("BLOQUEADO: AJUSTE NÃO PODE FICAR NEGATIVO!!");
		if((SimpleEnumStat.enumInIf(ajuste.getTipo()) != "DECREMENTO")&&(produto.get().getQuantidadeMax() != 0) && ((produto.get().getQuantidadeAtual() + ajuste.getQuantidade())) > (produto.get().getQuantidadeMax())) return ResponseEntity.ok("BLOQUEADO: AJUSTE EXCEDE ESTOQUE MÁXIMO!!");
		if(SimpleEnumStat.enumInIf(ajuste.getTipo()) == "INCREMENTO") produto.get().setQuantidadeAtual(produto.get().getQuantidadeAtual() + ajuste.getQuantidade());
		else if(SimpleEnumStat.enumInIf(ajuste.getTipo()) == "DECREMENTO") produto.get().setQuantidadeAtual(produto.get().getQuantidadeAtual() - ajuste.getQuantidade());
		else if(SimpleEnumStat.enumInIf(ajuste.getTipo()) == "AJUSTE") produto.get().setQuantidadeAtual(ajuste.getQuantidade());;
		ajuste.setProduto(produto.get());
		
		Optional<Usuario> nome = userRepository.findByUsername(ajuste.getNome());
		if(nome.isPresent() == true )ajuste.setNome(nome.get().getNome());
		
		produtoRepository.save(produto.get());
		Ajuste producaoSalva = ajusteRepository.save(ajuste);
		return ResponseEntity.status(HttpStatus.OK).body(producaoSalva);
	}
}
