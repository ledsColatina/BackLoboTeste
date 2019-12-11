package com.LoboProject.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import com.LoboProject.domain.SimpleEnum.Status;


@Entity
public class Pedido {
	
	@Id
	private Long numero;
	
	@NotNull
	private Long prioridade;
	
	@NotNull
	private String nomeCliente;
	
	@NotNull
	private String endereco;
	
	@NotNull
	private Status status;
	
	private String nmrNota;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy ")
	private Date dataEmbalagem;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy ")
	private Date dataExpedicao;
	
	//Produto


	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public Long getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(Long prioridade) {
		this.prioridade = prioridade;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getNmrNota() {
		return nmrNota;
	}

	public void setNmrNota(String nmrNota) {
		this.nmrNota = nmrNota;
	}

	public Date getDataEmbalagem() {
		return dataEmbalagem;
	}

	public void setDataEmbalagem(Date dataEmbalagem) {
		this.dataEmbalagem = dataEmbalagem;
	}

	public Date getDataExpedicao() {
		return dataExpedicao;
	}

	public void setDataExpedicao(Date dataExpedicao) {
		this.dataExpedicao = dataExpedicao;
	}
	
	
}
