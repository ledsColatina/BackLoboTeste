package com.LoboProject.domain;


import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import com.LoboProject.domain.SimpleEnum.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@Entity
@Data
public class Pedido{
	
	@Id
	private Long codigo;
	
	private Long prioridade = (long)0;
	
	@NotNull
	private String nomeCliente;
	
	@NotNull
	private String endereco;
	
	private Status status = SimpleEnum.Status.FILA;
	
	private String nmrNota;
	
	private String dataChegada;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy ")
	private Date dataEmbalagem;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy ")
	private Date dataExpedicao;
	
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	List<PedidoProduto> itens;
	
	@JsonIgnore
	private String transportadora;
	
	@JsonIgnore
	private String vendedor;
	
	@JsonIgnore
	private String condicoes;
	
	@Transient
	private int volumes =  0;


	
}
