package com.LoboProject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.LoboProject.domain.Producao;
import com.LoboProject.domain.Produto;
import com.LoboProject.domain.Usuario;
import com.LoboProject.repository.ProdutoRepository;
import com.LoboProject.repository.RegistrarProducaoRepository;
import com.LoboProject.repository.UsuarioRepository;

@Service
public class ProducaoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private RegistrarProducaoRepository producaoRepository;
	
	@Autowired
	private UsuarioRepository userRepository;

	
	
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
		if(producao.getQuantidade() <= 0) return ResponseEntity.status(HttpStatus.CONFLICT).body("Quantidade para Produção Inválida");
		else if((produto.get().getQuantidadeMax() != 0 )&&(produto.get().getQuantidadeMax() < (produto.get().getQuantidadeAtual()))) return ResponseEntity.status(HttpStatus.CONFLICT).body("Quantidade para Produção Inválida Excede Estoque Máximo!!");
		else if(verificarComp(produto.get(), producao) == 1) {
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
			Optional<Usuario> nome = userRepository.findByUsername(producao.getNome());
			if(nome.isPresent() == true )producao.setNome(nome.get().getNome());
			Producao producaoSalva = producaoRepository.save(producao);
			return ResponseEntity.status(HttpStatus.OK).body(producaoSalva);
		}
		else return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}
	
	public List<Producao> porUser(String username){
		Optional<Usuario> usuario = userRepository.findByUsername(username);
		List<Producao> list = new ArrayList<>();
		if(usuario.isPresent() == false) return null;
		else {
			for (int i = 0; i < usuario.get().getSetores().size(); i++) {
				list.addAll(producaoRepository.findByProduto_Setor_id(usuario.get().getSetores().get(i).getId()));
			}if (usuario.get().isTipo() == true) {
				list = producaoRepository.findAllByOrderByCodigoDesc();
				return list;
			}
		}
		return list;
	}
	
	public List<Producao> ordenarProducao(List<Producao> lista){
		Producao aux;
		for(int i = 0; i < lista.size()-1; i++) {
			if(lista.get(i).getCodigo() < lista.get(i+1).getCodigo()) {
				aux = lista.get(i);
				lista.set(i, lista.get(i+1));
				lista.set(i+1, aux);
				i--;
			}
		}
		return lista;
	}
	
	
	
}
