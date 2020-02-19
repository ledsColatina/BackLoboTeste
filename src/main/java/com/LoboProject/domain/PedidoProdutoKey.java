package com.LoboProject.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;


@SuppressWarnings("serial")
@Embeddable
@Data
public class PedidoProdutoKey implements Serializable{
	
	@Column(name = "produto_codigo", updatable = true)
    String produtoCodigo;
 
    @Column(name = "pedido_codigo", updatable = true)
    Long pedidoCodigo;    
    
}
