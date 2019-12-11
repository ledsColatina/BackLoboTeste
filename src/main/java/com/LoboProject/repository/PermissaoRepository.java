package com.LoboProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.LoboProject.domain.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long>{

	
	public Permissao findByDescricao(String descricao);
	
}
