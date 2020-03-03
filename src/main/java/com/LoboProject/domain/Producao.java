package com.LoboProject.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;


@Entity
@Data
public class Producao{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@NotNull
	private Long quantidade;
	
	@ManyToOne
	@JoinColumn(name = "id_produto")
	private Produto produto;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy ")
	private Date data;
	
	private String nome;
	
	
}
