package com.LoboProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.LoboProject.domain.Composicao;

public interface ComposicaoRepository extends JpaRepository<Composicao, Long>{

	void deleteByprodutoParte_codigo(String id);

	Object findAllByProdutoParte_codigo(String string);
	
	
}
