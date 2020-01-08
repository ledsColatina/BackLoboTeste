package com.LoboProject.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.LoboProject.domain.Ajuste;

public interface AjusteRepository extends JpaRepository<Ajuste, Long> {

	List<Ajuste> findAllByOrderByCodigoDesc();
}
