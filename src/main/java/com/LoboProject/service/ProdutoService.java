package com.LoboProject.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	
	// Procura_Bugs
	public int ProcurarBugs(Produto produto,List<Composicao> lista, String id, int a) {
		List<Composicao> compInic = lista; // Declaração_De_Lista_Composicao
		List<Produto> listProduto = produtoRepository.findByComposicao_ProdutoParte_codigo(id);  //  SQL_PARA_BUSCA_DE_PRODUTOPARTE
		int i, j;
		if (a == -1) return -1;		// Se_a_é_-1_significa_que_existem_bugs_na_composicao;
		if((listProduto == null)||(listProduto.isEmpty())) return 0;	// Se_a_lista_estiver_vazia_retorna_0_OK_procedimento_completo
		
		for(i = 0; i < listProduto.size(); i++) {		// Percorrer_A_Lista_Baseada_na_Busca_SQL_de_ProdutoParte
			for(j = 0; j < produto.getComposicao().size(); j++) { 	// Percorrer_A_LISTA_DA_COMPOSICAO_DO_PRODUTO
	
				if ((listProduto.contains(compInic.get(j).getProdutoParte()) ==  true)) {	//Verificar_se_Existe_Produto_que_não_podem ter_na_Composicao
					a = -1;
					return -1;	// Se_tiver_retorna_-1
				}
			}
		}
		
		ProcurarBugs(listProduto.get(a), lista, listProduto.get(a).getCodigo(), a++); //Chamada_recursiva_passando_o_proximo_e_o_resultado_se_contem_bugs_ou_nao
		return -1;
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
	
	@SuppressWarnings("unlikely-arg-type")
	public ResponseEntity<?> atualizarProduto (Produto produto, String id) {
		if(produto.getComposicao().contains(produtoRepository.findById(id))) return ResponseEntity.status(HttpStatus.CONFLICT).build();
		
		else if (ProcurarBugs(produto,produto.getComposicao(), id , 0)  == -1) return ResponseEntity.ok("BLOQUEADO: Loop Encontrado na Composição");
		
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
	
}
