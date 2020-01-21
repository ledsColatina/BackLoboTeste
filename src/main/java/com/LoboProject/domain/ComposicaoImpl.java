package com.LoboProject.domain;

import org.springframework.data.querydsl.binding.QuerydslPredicate;

@QuerydslPredicate
public class ComposicaoImpl {
	
	public String id_produtoTodo;
	
	public String id_produtoParte;
	
	public ComposicaoImpl(String id_produtoTodo, String id_produtoParte) {
		super();
		this.id_produtoTodo = id_produtoTodo;
		this.id_produtoParte = id_produtoParte;
	}

	public String getId_produtoTodo() {
		return id_produtoTodo;
	}

	public void setId_produtoTodo(String id_produtoTodo) {
		this.id_produtoTodo = id_produtoTodo;
	}

	public String getId_produtoParte() {
		return id_produtoParte;
	}

	public void setId_produtoParte(String id_produtoParte) {
		this.id_produtoParte = id_produtoParte;
	}
	
}
