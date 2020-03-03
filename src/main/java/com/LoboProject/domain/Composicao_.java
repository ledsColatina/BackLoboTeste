package com.LoboProject.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import lombok.Data;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Composicao.class)
@Data
public abstract class Composicao_ {

	public static volatile SingularAttribute<Composicao, Produto> produtoParte;
	public static volatile SingularAttribute<Composicao, Long> quantidade;
	
}
