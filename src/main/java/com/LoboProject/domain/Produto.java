package com.LoboProject.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;


@Entity
@Data
public class Produto {
	
	@Id
	private String codigo;
	
	@NotEmpty(message = "Descricao Deverá ser informada")
	@NotNull(message = "Descricao Deverá ser informada")
	@Column(unique=true)
	private String descricao;
	
	private Long quantidadeMin = (long) 0;
	
	private Long quantidadeAtual = (long) 0;
	
	private Long quantidadeMax;
	
	@Transient
	private Long quantidadeAcumulada = (long) 0;
	
	@Transient 
	private Long quantidadePedidoDireto = (long) 0;
	
	
	@ManyToOne
	@JoinColumn(name = "id_Setor")
	private Setor setor;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="id_produto_todo")
	private List<Composicao> composicao;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((composicao == null) ? 0 : composicao.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((quantidadeAtual == null) ? 0 : quantidadeAtual.hashCode());
		result = prime * result + ((quantidadeMax == null) ? 0 : quantidadeMax.hashCode());
		result = prime * result + ((quantidadeMin == null) ? 0 : quantidadeMin.hashCode());
		result = prime * result + ((setor == null) ? 0 : setor.hashCode());
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
		Produto other = (Produto) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (composicao == null) {
			if (other.composicao != null)
				return false;
		} else if (!composicao.equals(other.composicao))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (quantidadeAtual == null) {
			if (other.quantidadeAtual != null)
				return false;
		} else if (!quantidadeAtual.equals(other.quantidadeAtual))
			return false;
		if (quantidadeMax == null) {
			if (other.quantidadeMax != null)
				return false;
		} else if (!quantidadeMax.equals(other.quantidadeMax))
			return false;
		if (quantidadeMin == null) {
			if (other.quantidadeMin != null)
				return false;
		} else if (!quantidadeMin.equals(other.quantidadeMin))
			return false;
		if (setor == null) {
			if (other.setor != null)
				return false;
		} else if (!setor.equals(other.setor))
			return false;
		return true;
	}



	
	

}
