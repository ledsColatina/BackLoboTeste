package com.LoboProject.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.LoboProject.domain.Pedido;
import com.LoboProject.domain.SimpleEnum;
import com.LoboProject.repository.PedidoRepository;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidorepository;
			
	public List<Pedido> listarSeparadamente(String string){
		if(string.equals("FILA")) {
			return pedidorepository.findByStatus(SimpleEnum.Status.FILA);
		}else if (string.equals("EM_PRODUCAO")) {
			return pedidorepository.findByStatus(SimpleEnum.Status.EM_PRODUCAO);
		}else if (string.equals("EMBALADO")) {
			return pedidorepository.findByStatus(SimpleEnum.Status.EMBALADO);
		}else if (string.equals("NOTA_EMITIDA")) {
			return pedidorepository.findByStatus(SimpleEnum.Status.NOTA_EMITIDA);
		}else if (string.equals("DESPACHADO")) {
			return pedidorepository.findByStatus(SimpleEnum.Status.DESPACHADO);
		}else {
			return null;
		}
	}
	
	public List<Pedido> gerarNota (List<Pedido> pedidos){
		for(int i = 0; i < pedidos.size(); i++) {
			if(pedidos.get(i).getStatus().equals(SimpleEnum.Status.EM_PRODUCAO)) pedidos.get(i).setStatus(SimpleEnum.Status.NOTA_EMITIDA);
		}
		return pedidos;
	}
	
	public List<Pedido> expedir (List<Pedido> pedidos){
		for(int i = 0; i < pedidos.size(); i++) {
			if(pedidos.get(i).getStatus().equals(SimpleEnum.Status.NOTA_EMITIDA)) pedidos.get(i).setStatus(SimpleEnum.Status.DESPACHADO);
		}
		return pedidos;
	}
	
	public List<Pedido> criarFila (List<Pedido> pedidos){
		Optional<Pedido> ultimo = pedidorepository.findTop1ByOrderByPrioridadeDesc();
		if(ultimo.isPresent() == true) {
			for(int i = 0; i < pedidos.size(); i++) {
				ultimo = pedidorepository.findTop1ByOrderByPrioridadeDesc();
				if (ultimo.get().getPrioridade() == null) ultimo.get().setPrioridade((long) 1);
				pedidos.get(i).setPrioridade(ultimo.get().getPrioridade() + 1 );
				pedidos.get(i).setStatus(SimpleEnum.Status.EM_PRODUCAO);
			}
			return pedidos;
		}
		else {
			return null;
		}
		
	}
	
		
	
}