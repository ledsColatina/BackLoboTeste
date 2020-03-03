package com.LoboProject.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.LoboProject.domain.Producao;


public interface ProducaoRepository extends JpaRepository<Producao, Long>{


	List<Producao> findTop50ByOrderByCodigoDesc();

	List<Producao> findAllByOrderByCodigoDesc();
	
	List<Producao> findByProduto_Setor_id(long id);

	@Query(value = " select * from producao where producao.data BETWEEN NOW() - INTERVAL '30' DAY AND NOW();", nativeQuery = true)
	List<Producao> findByUltimasProducoes();
	
	@Query(value = " select * from producao where producao.id_produto.setor = ? producao.data BETWEEN NOW() - INTERVAL '30' DAY AND NOW();", nativeQuery = true)
	List<Producao> findByUltimasProducoesProdutos(Long valor);
}
