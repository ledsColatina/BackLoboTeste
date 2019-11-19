package com.LoboProject.repository.resumo;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.LoboProject.Projection.ResumoComposicao;
import com.LoboProject.domain.Composicao;
import com.LoboProject.domain.Composicao_;
import com.LoboProject.domain.Produto_;


public class ComposicaoRepositoryImpl implements ComposicaoRepositoryQuery{
	
	@PersistenceContext
	private EntityManager manager;
	
	public List<ResumoComposicao> resumir() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoComposicao> criteria = builder.createQuery(ResumoComposicao.class);
		Root<Composicao> root = criteria.from(Composicao.class);
		
		criteria.select(builder.construct(ResumoComposicao.class
				, root.get(Composicao_.produtoParte).get(Produto_.codigo)
				, root.get(Composicao_.quantidade)));
		
		TypedQuery<ResumoComposicao> query = manager.createQuery(criteria);
		return query.getResultList();
	}

	
}
