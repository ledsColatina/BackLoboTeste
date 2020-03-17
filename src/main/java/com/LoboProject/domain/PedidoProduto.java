package com.LoboProject.domain;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@SuppressWarnings("serial")
@Entity
@Data
public class PedidoProduto implements Serializable{

	@EmbeddedId
	PedidoProdutoKey id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("pedido_codigo")
    @JoinColumn(name = "pedido_codigo")
	@JsonIgnore
    private Pedido pedido;
 
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("produto_codigo")
    @JoinColumn(name = "produto_codigo")
    private Produto produto;
 
    private int quantidade;
    
    @Transient
    private int visible = 1;
    
    
}
