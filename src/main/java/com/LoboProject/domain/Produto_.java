package com.LoboProject.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import lombok.Data;


@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Produto.class)
@Data
public abstract class Produto_ {
	
	public static volatile SingularAttribute<Produto, String> codigo;
	public static volatile SingularAttribute<Produto, String> descricao;
	public static volatile SingularAttribute<Produto, Setor> setor;
	public static volatile SingularAttribute<Produto, Long> quantidadeAtual;
	
	
	
}
