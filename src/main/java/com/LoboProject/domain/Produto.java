package com.LoboProject.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Entity
public class Produto {
	
	@Id
	private String codigo;
	
	@NotEmpty(message = "Descricao Deverá ser informada")
	@NotNull(message = "Descricao Deverá ser informada")
	private String descricao;
	
	private Long quantidadeMin = (long) 0;
	
	private Long quantidadeAtual = (long) 0;
	
	private Long quantidadeMax;
	
	@ManyToOne
	@JoinColumn(name = "id_Setor")
	private Setor setor;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="id_produto_todo")
	private List<Composicao> composicao;
	
	
	public Long getQuantidadeAtual() {
		return quantidadeAtual;
	}

	public void setQuantidadeAtual(Long quantidadeAtual) {
		this.quantidadeAtual = quantidadeAtual;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getQuantidadeMin() {
		return quantidadeMin;
	}

	public void setQuantidadeMin(Long quantidadeMin) {
		this.quantidadeMin = quantidadeMin;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public List<Composicao> getComposicao() {
		return composicao;
	}

	public void setComposicao(List<Composicao> composicao) {
		this.composicao = composicao;
	}

	public Long getQuantidadeMax() {
		return quantidadeMax;
	}

	public void setQuantidadeMax(Long quantidadeMax) {
		this.quantidadeMax = quantidadeMax;
	}

}
