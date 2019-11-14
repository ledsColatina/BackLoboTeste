package com.LoboProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.LoboProject.domain.RegistrarProducao;

public interface RegistrarProducaoRepository extends JpaRepository<RegistrarProducao, Long>{


	List<RegistrarProducao> findTop50ByOrderByCodigoDesc();

	

}
