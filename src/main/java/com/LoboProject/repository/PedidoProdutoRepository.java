package com.LoboProject.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.LoboProject.domain.PedidoProduto;
import com.LoboProject.domain.SimpleEnum.Status;

public interface PedidoProdutoRepository extends JpaRepository<PedidoProduto, Long>{
	
	public void deleteByPedido_codigo(Long codigo);
	
	public List<PedidoProduto> findByPedido_codigo(Long codigo);
	
	List<PedidoProduto> findByProduto_codigo(String codigo);

	public List<PedidoProduto> findByPedido_status(Status emProducao);

	public List<PedidoProduto> findByPedido_statusAndProduto_Setor_id(Status emProducao, Long setor_id);

	public List<PedidoProduto> findByPedido_statusAndProduto_Setor_idAndPedido_codigo(Status emProducao, Long id, Long codigo);

	public List<PedidoProduto> findByPedido_statusAndPedido_codigo(Status emProducao, Long codigo);

}
