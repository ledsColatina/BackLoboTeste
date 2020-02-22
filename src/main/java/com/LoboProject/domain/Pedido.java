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


@Entity
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
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
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


	public String getDataChegada() {
		return dataChegada;
	}

	public void setDataChegada(String dataChegada) {
		this.dataChegada = dataChegada;
	}

	

	public List<PedidoProduto> getItens() {
		return itens;
	}

	public void setItens(List<PedidoProduto> itens) {
		this.itens = itens;
	}

	public String getTransportadora() {
		return transportadora;
	}

	public void setTransportadora(String transportadora) {
		this.transportadora = transportadora;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public String getCondicoes() {
		return condicoes;
	}

	public void setCondicoes(String condicoes) {
		this.condicoes = condicoes;
	}

	public int getVolumes() {
		return volumes;
	}

	public void setVolumes(int volumes) {
		this.volumes = volumes;
	}

	
	
	
}
