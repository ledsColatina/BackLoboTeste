package com.LoboProject.Projection;

public class ResumoProduto {
	
	private String codigo;
	private String descricao;
	private String setor;
	private Long quantidadeAtual;
	
	
	public ResumoProduto(String codigo, String descricao, String setor, Long quantd) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.setor = setor;
		this.quantidadeAtual = quantd;
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

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public Long getQuantidadeAtual() {
		return quantidadeAtual;
	}

	public void setQuantidadeAtual(Long quantidadeAtual) {
		this.quantidadeAtual = quantidadeAtual;
	}

	
	
}
