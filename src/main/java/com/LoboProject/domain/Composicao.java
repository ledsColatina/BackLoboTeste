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
	
	private String id_produto_todo;
	

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
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getId_produto_todo() {
		return id_produto_todo;
	}

	public void setId_produto_todo(String id_produto_todo) {
		this.id_produto_todo = id_produto_todo;
	}
	
	
}
