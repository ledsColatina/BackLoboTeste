package com.LoboProject.repository.resumo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.LoboProject.Projection.ResumoProduto;
import com.LoboProject.domain.Produto;
import com.LoboProject.domain.Produto_;
import com.LoboProject.domain.Setor_;

public class ProdutoRepositoryImpl implements ProdutoRepositoryQuery{
	
	@PersistenceContext
	private EntityManager manager;
	
	public List<ResumoProduto> resumir() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoProduto> criteria = builder.createQuery(ResumoProduto.class);
		Root<Produto> root = criteria.from(Produto.class);
		
		criteria.select(builder.construct(ResumoProduto.class
				, root.get(Produto_.CODIGO), root.get(Produto_.DESCRICAO)
				, root.get(Produto_.SETOR).get(Setor_.DESCRICAO) , root.get(Produto_.QUANTIDADE_ATUAL)));
		
		TypedQuery<ResumoProduto> query = manager.createQuery(criteria);
		return query.getResultList();
	}

}
