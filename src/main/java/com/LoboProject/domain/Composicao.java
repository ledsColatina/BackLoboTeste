 package com.LoboProject.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.codehaus.jackson.annotate.JsonIgnore;
import lombok.Data;

@Entity
@Data
public class Composicao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long codigo;
	
	@NotNull
	private Long quantidade;
	
	@ManyToOne
	@JoinColumn(name = "id_produto_parte")
	private Produto produtoParte;
	
	private String id_produto_todo;

}
