package com.LoboProject.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
public class Setor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Descricao Deverá ser informada")
	private String descricao;
	
	@NotNull
	private boolean base;
	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public boolean isBase() {
		return base;
	}
	
	public void setBase(boolean base) {
		this.base = base;
	}


}
