package com.LoboProject.domain;

import org.springframework.data.querydsl.binding.QuerydslPredicate;
import lombok.Data;

@QuerydslPredicate
@Data
public class ComposicaoImpl {
	
	public String id_produtoTodo;
	
	public String id_produtoParte;
	
}
