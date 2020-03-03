package com.LoboProject.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import com.LoboProject.domain.SimpleEnum.Tipo;
import lombok.Data;


@Entity
@Data
public class Ajuste {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@NotEmpty
	private String justificativa;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy ")
	private Date data;
	
	@NotNull
	private Long quantidade;
	
	
	private Tipo tipo = SimpleEnum.Tipo.INCREMENTO;
	
	@ManyToOne
	@JoinColumn(name = "id_produto")
	private Produto produto;
	
	private String nome;
	
	
}
