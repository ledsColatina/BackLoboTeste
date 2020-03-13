package com.LoboProject.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.LoboProject.domain.Pedido;
import com.LoboProject.domain.SimpleEnum.Status;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{
	
	Optional<Pedido> findTop1ByOrderByPrioridadeDesc();
	
	List<Pedido> findByOrderByPrioridadeAsc();

	List<Pedido> findByStatus(Status fila);

	Optional<Pedido> findTop1ByOrderByPrioridadeAsc();
	
	@Query(value = " select * from pedido where pedido.status = 1 ORDER BY pedido.Prioridade Asc", nativeQuery = true)
	List<Pedido> findByStatusAndPrioridade();
}
