package com.LoboProject.repository.resumo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.LoboProject.Projection.ResumoProduto;
import com.LoboProject.domain.Produto;
import com.LoboProject.repository.ProdutoRepository;

public interface ProdutoRepositoryQuery{

	public List<ResumoProduto> resumir();
	
}
