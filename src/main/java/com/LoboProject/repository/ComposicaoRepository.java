package com.LoboProject.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.LoboProject.Projection.ResumoComposicao;
import com.LoboProject.domain.Composicao;


public interface ComposicaoRepository extends JpaRepository<Composicao, Long>{

	void deleteByprodutoParte_codigo(String id);

	List<Composicao> findAllByProdutoParte_codigo(String string);
	
	public List<ResumoComposicao> resumir();
	
	@Query(value = "select * from composicao where id_produto_parte = ? and id_produto_todo != ''", nativeQuery = true)
	List<Composicao> findAllProdutoTodo(String id_produto_parte);
}
