package com.LoboProject.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;


@SuppressWarnings("serial")
@Embeddable
public class PedidoProdutoKey implements Serializable{
	
	@Column(name = "produto_codigo")
    String produtoCodigo;
 
    @Column(name = "pedido_codigo")
    Long pedidoCodigo;

	public String getProdutoCodigo() {
		return produtoCodigo;
	}

	public void setProdutoCodigo(String produtoCodigo) {
		this.produtoCodigo = produtoCodigo;
	}

	public Long getCourseId() {
		return pedidoCodigo;
	}

	public void setCourseId(Long pedidoCodigo) {
		this.pedidoCodigo = pedidoCodigo;
	}

	public Long getPedidoCodigo() {
		return pedidoCodigo;
	}

	public void setPedidoCodigo(Long pedidoCodigo) {
		this.pedidoCodigo = pedidoCodigo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pedidoCodigo == null) ? 0 : pedidoCodigo.hashCode());
		result = prime * result + ((produtoCodigo == null) ? 0 : produtoCodigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PedidoProdutoKey other = (PedidoProdutoKey) obj;
		if (pedidoCodigo == null) {
			if (other.pedidoCodigo != null)
				return false;
		} else if (!pedidoCodigo.equals(other.pedidoCodigo))
			return false;
		if (produtoCodigo == null) {
			if (other.produtoCodigo != null)
				return false;
		} else if (!produtoCodigo.equals(other.produtoCodigo))
			return false;
		return true;
	}

	
    
    
}
