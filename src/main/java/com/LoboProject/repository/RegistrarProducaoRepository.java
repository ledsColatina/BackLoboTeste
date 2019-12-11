package com.LoboProject.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.LoboProject.domain.Producao;


public interface RegistrarProducaoRepository extends JpaRepository<Producao, Long>{


	List<Producao> findTop50ByOrderByCodigoDesc();

	List<Producao> findAllByOrderByCodigoDesc();

	

}
