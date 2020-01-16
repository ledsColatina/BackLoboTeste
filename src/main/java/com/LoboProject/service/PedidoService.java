package com.LoboProject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.LoboProject.domain.Pedido;
import com.LoboProject.domain.PedidoProduto;
import com.LoboProject.domain.PedidoProdutoKey;
import com.LoboProject.domain.Produto;
import com.LoboProject.domain.SimpleEnum;
import com.LoboProject.domain.Usuario;
import com.LoboProject.repository.PedidoProdutoRepository;
import com.LoboProject.repository.PedidoRepository;
import com.LoboProject.repository.ProdutoRepository;
import com.LoboProject.repository.UsuarioRepository;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidorepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private PedidoProdutoRepository pedidoProdutoRepository;
	
	@Autowired
	private UsuarioRepository userRepository;
	
	public List<Pedido> listarSeparadamente(String string){
		if(string.equals("FILA")) {
			return pedidorepository.findByStatus(SimpleEnum.Status.FILA);
		}else if (string.equals("EM_PRODUCAO")) {
			return pedidorepository.findByStatus(SimpleEnum.Status.EM_PRODUCAO);
		}else if (string.equals("EMBALADO")) {
			return pedidorepository.findByStatus(SimpleEnum.Status.EMBALADO);
		}else if (string.equals("NOTA_EMITIDA")) {
			return pedidorepository.findByStatus(SimpleEnum.Status.NOTA_EMITIDA);
		}else if (string.equals("DESPACHADO")) {
			return pedidorepository.findByStatus(SimpleEnum.Status.DESPACHADO);
		}else {
			return null;
		}
	}
	
	public List<Pedido> listarSeparadamentePrioridade(String string){
		List<Pedido> lista = pedidorepository.findByStatus(SimpleEnum.Status.EM_PRODUCAO);
		
		for(int i =0 ; i < lista.size() ; i++) {
			if(!lista.get(i).getStatus().equals(SimpleEnum.Status.EM_PRODUCAO)) {
				lista.remove(i);
			}
		}
		
		return lista;
	}
	
	public Pedido embalar (Pedido pedidos){
		if(pedidos.getStatus().compareTo(SimpleEnum.Status.EM_PRODUCAO) == 0) pedidos.setStatus(SimpleEnum.Status.EMBALADO);
		return pedidos;
	}
	
	public Pedido gerarNota (Pedido pedidos){
		if(pedidos.getStatus().compareTo(SimpleEnum.Status.EMBALADO) == 0) pedidos.setStatus(SimpleEnum.Status.NOTA_EMITIDA);
		return pedidos;
	}
	
	public Pedido expedir (Pedido pedidos){
		if(pedidos.getStatus().compareTo(SimpleEnum.Status.NOTA_EMITIDA) == 0) pedidos.setStatus(SimpleEnum.Status.DESPACHADO);
		return pedidos;
	}
	
	public List<Pedido> criarFila (List<Pedido> pedidos){
		Optional<Pedido> ultimo = pedidorepository.findTop1ByOrderByPrioridadeDesc();
		Long prioridade;
		if(ultimo.isPresent() == true) {
			for(int i = 0; i < pedidos.size(); i++) {
				if (ultimo.get().getPrioridade() == null) ultimo.get().setPrioridade((long) 1);
				ultimo = pedidorepository.findTop1ByOrderByPrioridadeDesc();
				prioridade = ultimo.get().getPrioridade() + 1;
				pedidos.get(i).setPrioridade(prioridade);
				pedidos.get(i).setStatus(SimpleEnum.Status.EM_PRODUCAO);
				pedidorepository.save(pedidos.get(i));
			}
			return pedidos;
		}
		else {
			return null;
		}
		
	}
	
	public List<PedidoProduto> itensPedido(Pedido pedido){
		List<PedidoProduto> lista = new ArrayList<>();
		for(int i = 0; i  < pedido.getItens().size(); i++) {
			PedidoProdutoKey chave = new PedidoProdutoKey();
			chave.setPedidoCodigo(pedido.getCodigo());
			chave.setProdutoCodigo(pedido.getItens().get(i).getProduto().getCodigo());
			PedidoProduto x = new PedidoProduto();
			x.setPedido(pedido);
			x.setProduto(pedido.getItens().get(i).getProduto());
			x.setQuantidade(pedido.getItens().get(i).getQuantidade());
			x.setId(chave);
			lista.add(x);
		}
		return lista;
	}
	
	public PedidoProduto minimizarMovimentoChave(long codigoPedido, String codigo, int quantidade) {
		Optional<Pedido> pedido = pedidorepository.findById(codigoPedido);
		Optional<Produto> embalagem = produtoRepository.findById(codigo);
		PedidoProdutoKey chave = new PedidoProdutoKey();
		chave.setPedidoCodigo(pedido.get().getCodigo());
		chave.setProdutoCodigo(embalagem.get().getCodigo());
		PedidoProduto x = new PedidoProduto();
		x.setPedido(pedido.get());
		x.setProduto(embalagem.get());
		x.setQuantidade(quantidade);
		x.setId(chave);
		return x;
	}
	
	
	public List<Pedido> atualizarQtd2 (List<Pedido> lista){
		List<Produto> produtos = produtoRepository.findAll();
		int x;
		int i = 0, j = 0;
		for(i = 0; i < lista.size() ; i++) {
			for(j = 0; j < lista.get(i).getItens().size(); j++) {
				if( produtos.get(produtos.indexOf(lista.get(i).getItens().get(j).getProduto())).getQuantidadeAtual() >= 0){
					x = produtos.indexOf(lista.get(i).getItens().get(j).getProduto());
					produtos.get(x).setQuantidadeAtual(produtos.get(x).getQuantidadeAtual() - lista.get(i).getItens().get(j).getQuantidade());
					lista.get(i).getItens().get(j).setProduto(produtos.get(x));
				}
				else {
					x = produtos.indexOf(lista.get(i).getItens().get(j).getProduto());
					produtos.get(x).setQuantidadeAtual((long) lista.get(i).getItens().get(j).getQuantidade());
					lista.get(i).getItens().get(j).setProduto(produtos.get(x));
				}
			}
		}
		return lista;
	} 
	
	
	
	
	public List<Pedido> atualizarQtd (List<Pedido> lista){
		List<Produto> aux = new ArrayList<Produto>();
		for(int i = 0; i < lista.size(); i++) {
			for(int j = 0; j < lista.get(i).getItens().size(); j++) {
				if(!aux.contains(lista.get(i).getItens().get(j).getProduto())){
					aux.add( produtoRepository.findById(lista.get(i).getItens().get(j).getProduto().getCodigo()).get());
				}
				if(aux.get(aux.indexOf(lista.get(i).getItens().get(j).getProduto())).getCodigo() == lista.get(i).getItens().get(j).getProduto().getCodigo()) {
				if((aux.get(aux.indexOf(lista.get(i).getItens().get(j).getProduto())).getQuantidadeAtual() >= 0)){
					aux.get(aux.indexOf(lista.get(i).getItens().get(j).getProduto())).setQuantidadeAtual(aux.get(aux.indexOf(lista.get(i).getItens().get(j).getProduto())).getQuantidadeAtual() - lista.get(i).getItens().get(j).getQuantidade());
					lista.get(i).getItens().get(j).getProduto().setQuantidadeAtual(aux.get(aux.indexOf(lista.get(i).getItens().get(j).getProduto())).getQuantidadeAtual());
				}
				if(aux.get(aux.indexOf(lista.get(i).getItens().get(j).getProduto())).getQuantidadeAtual() < 0) {
					lista.get(i).getItens().get(j).getProduto().setQuantidadeAtual(-(aux.get(aux.indexOf(lista.get(i).getItens().get(j).getProduto())).getQuantidadeAtual()));
					//aux.get(j).setQuantidadeAtual((long) -100);
				}
				else if(aux.get(aux.indexOf(lista.get(i).getItens().get(j).getProduto())).getQuantidadeAtual() == -100) {
					lista.get(i).getItens().get(j).getProduto().setQuantidadeAtual((long)lista.get(i).getItens().get(j).getQuantidade());
				}
					System.out.println("\n" + aux.get(aux.indexOf(lista.get(i).getItens().get(j).getProduto())).getQuantidadeAtual() + "	nome: " + aux.get(aux.indexOf(lista.get(i).getItens().get(j).getProduto())).getDescricao());
				}
				
			}
		}
		
		return lista;
	} 
	
	public Long maiorPrioridadeStatus() {
		List<Pedido> pedidos = pedidorepository.findByStatus(SimpleEnum.Status.EM_PRODUCAO);
		Long prioridade = (long) 10000;
		for(int i=0; i < pedidos.size(); i++) {
			if(pedidos.get(i).getPrioridade() < prioridade){
				prioridade = pedidos.get(i).getPrioridade();
			}
		}
		return prioridade;
	}
	
	public List<PedidoProduto> atualizarQtdP (List<PedidoProduto> lista){
		for(int i = 0; i < lista.size(); i++) {
			lista.get(i).getProduto().setQuantidadeAtual(produtoRepository.findById(lista.get(i).getProduto().getCodigo()).get().getQuantidadeAtual() - lista.get(i).getQuantidade());
			if(lista.get(i).getProduto().getQuantidadeAtual() >= 0) {
				lista.remove(i);
				i--;
			}
		}

		for(int i = 0; i < lista.size(); i++) {
			for(int j = 0; j < lista.size() ; j++) {
				if((lista.get(i).getProduto().getCodigo() == lista.get(j).getProduto().getCodigo())&&(j != i)) {
					lista.remove(j);
				}
			}
		}
		return lista;
	} 
	
	public List<PedidoProduto> filtroPorUser(String username){
		Optional<Usuario> usuario = userRepository.findByUsername(username);
		List<PedidoProduto> list = new ArrayList<>();
		int i;
		if(usuario.isPresent() == false) return null;
		else {
			for(i = 0; i < usuario.get().getSetores().size(); i++) {
				list.addAll(pedidoProdutoRepository.findByPedido_statusAndProduto_Setor_id(SimpleEnum.Status.EM_PRODUCAO, usuario.get().getSetores().get(i).getId()));
			}
			if(usuario.get().isTipo() == true) {
				list = pedidoProdutoRepository.findByPedido_status(SimpleEnum.Status.EM_PRODUCAO);
				return list;
			}
			
		}
		return list;

	}
	
	public List<PedidoProduto> filtroPorUserSetor(String username, Long codigo){
		Optional<Usuario> usuario = userRepository.findByUsername(username);
		List<PedidoProduto> list = new ArrayList<>();
		int i;
		if(usuario.isPresent() == false) return null;
		else {
			for(i = 0; i < usuario.get().getSetores().size(); i++) {
				list.addAll(pedidoProdutoRepository.findByPedido_statusAndProduto_Setor_idAndPedido_codigo(SimpleEnum.Status.EM_PRODUCAO, usuario.get().getSetores().get(i).getId(), codigo));
			}
			if(usuario.get().isTipo() == true) {
				list = pedidoProdutoRepository.findByPedido_statusAndPedido_codigo(SimpleEnum.Status.EM_PRODUCAO, codigo);
			}
			
		}
		return list;

	}
	
	public String DiminuirEmbalagem(long codigoPedido, String codigo, int quantidade) {
		Optional<Pedido> pedido = pedidorepository.findById(codigoPedido);
		int i;
		for(i = 0; i < pedido.get().getItens().size(); i++){
			Optional <Produto> prod = produtoRepository.findById(pedido.get().getItens().get(i).getProduto().getCodigo());
			if(prod.get().getQuantidadeAtual() - quantidade >= 0){
				prod.get().setQuantidadeAtual(prod.get().getQuantidadeAtual() - quantidade);
			}else {
				return ("Produto Insuficiente");
			}
		}
		return ("OK");
	}
	

	public String DiminuirEmbalagem2(long codigoPedido) {
		Optional<Pedido> pedido = pedidorepository.findById(codigoPedido);
		int i;
		for(i = 0; i < pedido.get().getItens().size(); i++){
			Optional <Produto> prod = produtoRepository.findById(pedido.get().getItens().get(i).getProduto().getCodigo());
			if(prod.get().getQuantidadeAtual() - pedido.get().getItens().get(i).getQuantidade() >= 0){
				prod.get().setQuantidadeAtual(prod.get().getQuantidadeAtual() - pedido.get().getItens().get(i).getQuantidade());
				produtoRepository.save(prod.get());
			}else {
				return ("Produto Insuficiente");
			}
		}
		return ("OK");
	}
	
	@Transactional
	public int deletar(long codigo) {
		pedidoProdutoRepository.deleteByPedido_codigo(codigo);
		pedidorepository.deleteById(codigo);
		return 1;
	}
	
}