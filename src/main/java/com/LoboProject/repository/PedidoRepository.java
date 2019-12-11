package com.LoboProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.LoboProject.domain.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{
	
}
