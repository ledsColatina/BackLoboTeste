package com.LoboProject.Projection;

import java.sql.Date;

import lombok.Data;

@Data
public class ResumoProducao {

	private String codigo;
	private String nome;
	private String descricao;
	private Long quantidade;
	private Date data;
	
	public ResumoProducao(String codigo, String nome, String descricao, Long quantidade, Date data) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.data = data;
	}
	
	
}
