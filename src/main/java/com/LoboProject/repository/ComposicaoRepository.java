package com.LoboProject.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.LoboProject.Projection.ResumoComposicao;
import com.LoboProject.domain.Composicao;

public interface ComposicaoRepository extends JpaRepository<Composicao, Long>{

	void deleteByprodutoParte_codigo(String id);

	Object findAllByProdutoParte_codigo(String string);
	
	public List<ResumoComposicao> resumir();
	
}
