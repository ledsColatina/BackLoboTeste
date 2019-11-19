package com.LoboProject.Projection;

public class ResumoComposicao {
	
	private String produtoParte;
	private Long quantidade;
	
	
	
	public ResumoComposicao(String produtoParte, Long quantidade) {
		this.produtoParte = produtoParte;
		this.quantidade = quantidade;
	}
	
	public String getProdutoParte() {
		return produtoParte;
	}
	public void setProdutoParte(String produtoParte) {
		this.produtoParte = produtoParte;
	}
	public Long getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}
	
	
}
