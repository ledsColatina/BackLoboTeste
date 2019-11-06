package com.LoboProject.Projection;

import java.util.List;

public class ResumoProduto {
	
	private String codigo;
	private String descricao;
	private int Setor_id;
	private List<String> codigoComposicao;
	
	
	public ResumoProduto(String codigo, String descricao, int setor_id, List<String> codigoComposicao) {
		this.codigo = codigo;
		this.descricao = descricao;
		Setor_id = setor_id;
		this.codigoComposicao = codigoComposicao;
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
	public int getSetor_id() {
		return Setor_id;
	}
	public void setSetor_id(int setor_id) {
		Setor_id = setor_id;
	}
	public List<String> getCodigoComposicao() {
		return codigoComposicao;
	}
	public void setCodigoComposicao(List<String> codigoComposicao) {
		this.codigoComposicao = codigoComposicao;
	}
	
}
