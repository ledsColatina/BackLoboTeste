package com.LoboProject.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;


@Entity
@Data
public class Produto {
	
	@Id
	private String codigo;
	
	@NotEmpty(message = "Descricao Deverá ser informada")
	@NotNull(message = "Descricao Deverá ser informada")
	@Column(unique=true)
	private String descricao;
	
	private Long quantidadeMin = (long) 0;
	
	private Long quantidadeAtual = (long) 0;
	
	private Long quantidadeMax;
	
	@ManyToOne
	@JoinColumn(name = "id_Setor")
	private Setor setor;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="id_produto_todo")
	private List<Composicao> composicao;

	


}
