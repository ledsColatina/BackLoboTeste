package com.LoboProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.LoboProject.domain.Setor;

public interface SetorRepository extends JpaRepository<Setor, Long>{

	Optional<Setor> findBydescricao(String descricao);

}
