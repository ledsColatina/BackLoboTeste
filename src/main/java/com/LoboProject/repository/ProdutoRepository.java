package com.LoboProject.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.LoboProject.Projection.ResumoProduto;
import com.LoboProject.domain.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, String>{

	public void deleteByCodigo(String codigo);

	public List<Produto> findBySetor_id(Long id_Setor);
	
	public List<ResumoProduto> resumir();

	public List<Produto> findByComposicao_ProdutoParte_codigo(String codigo);

	public List<Produto> findBySetor_descricao(String string);

	public Produto findBydescricao(String descricao);
	
	@Query(value = "select * from produto where produto.quantidade_atual < produto.quantidade_min", nativeQuery = true)
	List<Produto> findAllEstoque();
}
