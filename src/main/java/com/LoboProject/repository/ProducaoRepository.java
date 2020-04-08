package com.LoboProject.repository;

import java.sql.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.LoboProject.domain.Producao;


public interface ProducaoRepository extends JpaRepository<Producao, Long>{


	List<Producao> findTop50ByOrderByCodigoDesc();

	List<Producao> findAllByOrderByCodigoDesc();
	
	List<Producao> findByProduto_Setor_id(long id);

	@Query(value = " select * from producao where producao.data BETWEEN NOW() - INTERVAL '30' DAY AND NOW();", nativeQuery = true)
	List<Producao> findByUltimasProducoes();
	
	@Query(value = " select * from producao where producao.data BETWEEN to_date( :data, 'dd/mm/yyyy') and to_date( :data2, 'dd/mm/yyyy')", nativeQuery = true)
	List<Producao> findByUltimasProducoesA(String data, String data2);
	
	List<Producao> findAllByDataLessThanEqualAndDataGreaterThanEqual(Date data, Date data2);
	
	@Query(value = " select * from producao where producao.id_produto.setor = ? producao.data BETWEEN NOW() - INTERVAL '30' DAY AND NOW();", nativeQuery = true)
	List<Producao> findByUltimasProducoesProdutos(Long valor);

	List<Producao> findAllByOrderByCodigoDesc(Pageable pageable);
	
	Page<Producao> findByNomeOrderByCodigoDesc(String nome, Pageable pageable);
}
