package com.LoboProject.Projection;

import lombok.Data;

@Data
public class ResumoProduto {
	
	private String codigo;
	private String descricao;
	private String setor;
	private Long quantidadeAtual;
	
	public ResumoProduto(String codigo, String descricao, String setor, Long quantidadeAtual) {
		super();
		this.codigo = codigo;
		this.descricao = descricao;
		this.setor = setor;
		this.quantidadeAtual = quantidadeAtual;
	}
	
	
	
}
