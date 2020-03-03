package com.LoboProject.Projection;

import lombok.Data;

@Data
public class ResumoComposicao {
	
	private String produtoParte;
	private Long quantidade;
	
	
	public ResumoComposicao(String produtoParte, Long quantidade) {
		super();
		this.produtoParte = produtoParte;
		this.quantidade = quantidade;
	}
	
	
	
}
