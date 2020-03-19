package com.LoboProject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.LoboProject.domain.Pedido;
import com.LoboProject.domain.PedidoProduto;
import com.LoboProject.domain.PedidoProdutoKey;
import com.LoboProject.domain.Produto;
import com.LoboProject.domain.SimpleEnum;
import com.LoboProject.domain.Usuario;
import com.LoboProject.repository.PedidoProdutoRepository;
import com.LoboProject.repository.PedidoRepository;
import com.LoboProject.repository.ProdutoRepository;
import com.LoboProject.repository.SetorRepository;
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
	
	@Autowired
	private SetorRepository setorRepository;
	
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
		int i = 0;
		if(ultimo.isPresent()) {
			for(i = 0; i < pedidos.size(); i++) {
				ultimo = pedidorepository.findTop1ByOrderByPrioridadeDesc();
				prioridade = ultimo.get().getPrioridade() + (i+1);
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
	
	public long listarultimaprioridade() {
		Optional<Pedido> ultimo = pedidorepository.findTop1ByOrderByPrioridadeDesc();
		if(ultimo.isPresent()) {
			System.out.println("passei " + ultimo.get().getCodigo() + "  " + ultimo.get().getPrioridade());
			return ultimo.get().getPrioridade();
		}
		else return 0;
		
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
			
			lista.get(i).setQuantidadeTotalEstoqueMin((int) (lista.get(i).getProduto().getQuantidadeMin() + 0));
			lista.get(i).getProduto().setQuantidadeAtual(produtoRepository.findById(lista.get(i).getProduto().getCodigo()).get().getQuantidadeAtual() - lista.get(i).getQuantidade());
			lista.get(i).setQuantidadeTotalPedidos((int)(lista.get(i).getProduto().getQuantidadeAtual() + lista.get(i).getProduto().getQuantidadeMin()));
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
	
	@SuppressWarnings("unlikely-arg-type")
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
			}else if (!usuario.get().getSetores().contains(setorRepository.findBydescricao("Montagem"))) {
				list.addAll(pedidoProdutoRepository.findByPedido_statusAndProduto_Setor_id(SimpleEnum.Status.EM_PRODUCAO,(long) 3));
			}
			
		}
		return list;

	}
	

	public List<Pedido> quebrarDemandas(List<Pedido> lista, String username){
		int i,j, k;
		for(i = 0; i < lista.size(); i++) {
			for(j = 0 ; j < lista.get(i).getItens().size();j++) {
				for(k = 0; k < lista.get(i).getItens().get(j).getProduto().getComposicao().size(); k++) {
					PedidoProdutoKey chave = new PedidoProdutoKey();
					chave.setPedidoCodigo(lista.get(i).getCodigo());
					chave.setProdutoCodigo(lista.get(i).getItens().get(j).getProduto().getComposicao().get(k).getProdutoParte().getCodigo());
					PedidoProduto pedidoProduto = new PedidoProduto();
					pedidoProduto.setVisible(1);
					pedidoProduto.setPedido(lista.get(i));
					pedidoProduto.setProduto(lista.get(i).getItens().get(j).getProduto().getComposicao().get(k).getProdutoParte());
					pedidoProduto.setQuantidade((int)(lista.get(i).getItens().get(j).getQuantidade() * lista.get(i).getItens().get(j).getProduto().getComposicao().get(k).getQuantidade()));
					pedidoProduto.setQuantidadeTotalEstoqueMin((int)(pedidoProduto.getProduto().getQuantidadeMin() - 0));
					//pedidoProduto.set
					//pedidoProduto.setProduzir(lista.contains(o));
					lista.get(i).getItens().add(pedidoProduto);
					/*if(i == 0) {
						if(lista.get(i).getItens().get(j).getProduto().getQuantidadeAtual() < lista.get(i).getItens().get(j).getQuantidade()) {
							pedidoProduto.setQuantidade((int) (lista.get(i).getItens().get(j).getProduto().getComposicao().get(k).getQuantidade()
									* (lista.get(i).getItens().get(j).getQuantidade() - (lista.get(i).getItens().get(j).getProduto().getQuantidadeAtual()))));
							lista.get(i).getItens().add(pedidoProduto);
						}	
					}else {
							pedidoProduto.setQuantidade((int) (lista.get(i).getItens().get(j).getProduto().getComposicao().get(k).getQuantidade()
									* (lista.get(i).getItens().get(j).getQuantidade())));
							lista.get(i).getItens().add(pedidoProduto);
					}
					if(pedidoProduto.getQuantidade() < pedidoProduto.getProduto().getQuantidadeAtual()) {
						lista.get(i).getItens().get(lista.get(i).getItens().indexOf(pedidoProduto)).setVisible(0);
					}*/
					
					
				}
			}
		}
		return lista;
	}
	
	
	
	
	public List<Pedido> formatarTirandoRepetidos(List<Pedido> lista, String username ){
		for(int i = 0; i < lista.size(); i++) {
			lista.get(i).setItens(formatarComposicao(lista.get(i).getItens(), username));
		}
		return lista;
	}
	
	public List<PedidoProduto> formatarComposicao(List<PedidoProduto> lista, String username){
		for(int i = 0; i < lista.size(); i++) {
			for(int j = 0;  j < lista.size(); j++) {
				if((lista.get(i).getProduto().getCodigo().equals(lista.get(j).getProduto().getCodigo()) && (i != j))) {
					lista.get(j).setQuantidade((lista.get(j).getQuantidade() + lista.get(i).getQuantidade()));
					lista.remove(i);
					if(i > 0)i--;
				}
			}
		}
		lista = deixarApenasdoSetor(lista, username);
		return lista;
	}
	
	public List<PedidoProduto> deixarApenasdoSetor(List<PedidoProduto> lista, String username){
		Optional<Usuario> usuario = userRepository.findByUsername(username);
		
		for(int i =0; i < lista.size(); i++) {
			if((!usuario.get().getSetores().contains(lista.get(i).getProduto().getSetor()) && (!usuario.get().isTipo()))) {
				lista.remove(i);
				i--;
			}
		}
		return lista;
	}
	
	public List<PedidoProduto> formatarComposicaoSemSomar(List<PedidoProduto> lista){
		for(int i = 0; i < lista.size(); i++) {
			for(int j = 0;  j< lista.size(); j++) {
				if(lista.get(i).getProduto().getCodigo().equals(lista.get(j).getProduto().getCodigo()) && (i != j)) {
					lista.remove(j);
					j--;
				}
			}
		}
		
		return lista;
	}
	
	public ResponseEntity<List<Pedido>> buscarDemandas(@PathVariable String username){
		List <Pedido> lista = listarSeparadamente("EM_PRODUCAO");
		for(int i = 0; i < lista.size(); i++) {
			lista.get(i).setItens(pedidoProdutoRepository.findByPedido_statusAndPedido_codigo(SimpleEnum.Status.EM_PRODUCAO, lista.get(i).getCodigo()));
		}
		lista = ordernarPorPrioridade(lista);
		lista.addAll(estoqueMin(username));
		lista = quebrarDemandas(lista, username);
		lista = formatarTirandoRepetidos(lista, username);
		return !lista.isEmpty() ? ResponseEntity.ok(lista) : ResponseEntity.notFound().build() ;
	}
	
	public ResponseEntity<List<PedidoProduto>> buscarDemandasProduto(@PathVariable String username){
		List<PedidoProduto> lista = new ArrayList<PedidoProduto>();
		List<Pedido> aux = buscarDemandas(username).getBody();
		for(int i = 0; i < aux.size(); i++)  lista.addAll(atualizarQtdP(aux.get(i).getItens()));
		lista = formatarComposicaoSemSomar(lista);
		return ResponseEntity.ok().body(lista);
	}
	
	
	public String DiminuirEmbalagem(long codigoPedido, String codigo, int quantidade) {
		Optional<Pedido> pedido = pedidorepository.findById(codigoPedido);
		int i;
		for(i = 0; i < pedido.get().getItens().size(); i++){
			Optional <Produto> prod = produtoRepository.findById(pedido.get().getItens().get(i).getProduto().getCodigo());
			if((prod.get().getQuantidadeAtual() - quantidade) < 0){
				return ("Produto Insuficiente");
			}
		}
		return ("OK");
	}
	
	public String AumentarEmbalagem(long codigoPedido) {
		Optional<Pedido> pedido = pedidorepository.findById(codigoPedido);
		int i;
		for(i = 0; i < pedido.get().getItens().size(); i++){
			Optional <Produto> prod = produtoRepository.findById(pedido.get().getItens().get(i).getProduto().getCodigo());
			prod.get().setQuantidadeAtual(prod.get().getQuantidadeAtual() + pedido.get().getItens().get(i).getQuantidade());
			produtoRepository.save(prod.get());
		}
		return ("OK");
	}
	
	public List<Pedido> contagemVolumes(List<Pedido> lista){
		for(int i = 0; i < lista.size(); i++) {
			for(int j = 0; j < lista.get(i).getItens().size(); j++) {
				if(lista.get(i).getItens().get(j).getProduto().getSetor().getDescricao().equalsIgnoreCase("Embalagem")) {
					lista.get(i).setVolumes(lista.get(i).getVolumes() + lista.get(i).getItens().get(j).getQuantidade());
				}
				else if(!lista.get(i).getItens().get(j).getProduto().getComposicao().isEmpty()) {
					for(int k = 0; k < lista.get(i).getItens().get(j).getProduto().getComposicao().size(); k++) {
						if(lista.get(i).getItens().get(j).getProduto().getComposicao().get(k).getProdutoParte().getSetor().equals("Embalagem")) {
							lista.get(i).setVolumes(lista.get(i).getVolumes() + (int)(lista.get(i).getItens().get(j).getProduto().getComposicao().get(k).getQuantidade() * lista.get(i).getItens().get(j).getQuantidade()));
						}
					}
				}
			}
		}
		return lista;
	}
	
	public List<Pedido> modificarParaEmbalagens(List<Pedido> lista){
		for(int i = 0; i < lista.size(); i++) {
			for(int j = 0; j < lista.get(i).getItens().size(); j++) {
				if(!lista.get(i).getItens().get(j).getProduto().getSetor().getDescricao().equalsIgnoreCase("Embalagem")) {
					lista.get(i).getItens().remove(j);
					j--;
				}
				else if(!lista.get(i).getItens().get(j).getProduto().getComposicao().isEmpty()) {
					for(int k = 0; k < lista.get(i).getItens().get(j).getProduto().getComposicao().size(); k++) {
						if(lista.get(i).getItens().get(j).getProduto().getComposicao().get(k).getProdutoParte().getSetor().equals("Embalagem")) {
							PedidoProduto produto = new PedidoProduto();
							produto.setPedido(lista.get(i));
							produto.setProduto(lista.get(i).getItens().get(j).getProduto());
							produto.setQuantidade((int)(lista.get(i).getItens().get(j).getQuantidade() * lista.get(i).getItens().get(j).getProduto().getComposicao().get(k).getQuantidade()));
							lista.get(i).getItens().add(produto);
						}
					}
				}
			}
		}
		
		return lista;
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
	
	public List<Pedido> ordernarPorPrioridade(List<Pedido> lista){
		lista = pedidorepository.findByStatusAndPrioridade();
		return lista;
	}
	
	@Transactional
	public int deletar(long codigo) {
		Optional<Pedido> pedido = pedidorepository.findById(codigo);
		if((pedido.get().getStatus().equals(SimpleEnum.Status.EMBALADO)) || (pedido.get().getStatus().equals(SimpleEnum.Status.NOTA_EMITIDA))){
			AumentarEmbalagem(codigo); 	// Caso_Exclua_Precisa_Incrementar_Novamente
		}
		pedidoProdutoRepository.deleteByPedido_codigo(codigo);
		pedidorepository.deleteById(codigo);
		return 1;
	}
	
	public List<Pedido> estoqueMin(String username){
		Pedido pedido = new Pedido();
		pedido.setCodigo((long) 909090);
		pedido.setNomeCliente("Estoque Minimo");
		pedido.setPrioridade((long)9999);
		pedido.setEndereco("Local");
		pedido.setStatus(SimpleEnum.Status.EM_PRODUCAO);
		List<Produto> lista = produtoRepository.findAllEstoque();
		List<PedidoProduto> itens = new ArrayList<>();
		
		for(int i = 0; i  < lista.size(); i++) {
			PedidoProduto item = new PedidoProduto();
			item.setProduto(lista.get(i));
			item.setPedido(pedido);
			item.setQuantidade(lista.get(i).getQuantidadeMin().intValue());
			itens.add(item);
		}
		pedido.setItens(itens);
		List<Pedido> pedidos = new ArrayList<>();
		pedidos.add(pedido);
		//pedidos = quebrarDemandas(pedidos, username);
		return pedidos;
	}
	
	
}