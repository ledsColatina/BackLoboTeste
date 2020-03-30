package com.LoboProject.domain;

import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import lombok.Data;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Composicao.class)
@Data
public class Producao_ {

	public static volatile SingularAttribute<Producao, Produto> produto;
	public static volatile SingularAttribute<Producao, String> nome;
	public static volatile SingularAttribute<Producao, Date> data;
	public static volatile SingularAttribute<Producao, Long> quantidade;

}
