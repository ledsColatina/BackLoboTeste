 package com.LoboProject.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class Composicao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long codigo;
	
	@NotNull
	private Long quantidade;
	
	@ManyToOne
	@JoinColumn(name = "id_produto_parte")
	private Produto produtoParte;
	

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public Produto getProdutoParte() {
		return produtoParte;
	}

	public void setProdutoParte(Produto produtoParte) {
		this.produtoParte = produtoParte;
	}
	
	@JsonIgnore
	private Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
}
