package com.LoboProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LoboProject.domain.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, String>{

	public void deleteByCodigo(String codigo);

	public List<Produto> findBySetor_id(Long id_Setor);
	
}
