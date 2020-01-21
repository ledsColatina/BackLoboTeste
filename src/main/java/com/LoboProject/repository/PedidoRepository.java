package com.LoboProject.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.LoboProject.domain.Pedido;
import com.LoboProject.domain.SimpleEnum.Status;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{
	
	Optional<Pedido> findTop1ByOrderByPrioridadeDesc();
	
	List<Pedido> findByOrderByPrioridadeAsc();

	List<Pedido> findByStatus(Status fila);

	Optional<Pedido> findTop1ByOrderByPrioridadeAsc();

}
