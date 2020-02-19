package com.LoboProject.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import lombok.Data;


@Entity
@Table(name="usuario")
@Data
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@Column(unique=true)
	private String username;
	
	@Column(unique=true)
	private String nome;
	
	@Length(max=65) 
	private String senha;
	
	@NotNull
	private boolean tipo;
	
	@ManyToMany(fetch = FetchType.LAZY,  cascade = CascadeType.REMOVE)
	@JoinTable(name = "usuario_setor",
			joinColumns = {@JoinColumn(name="usuario_codigo")},
			inverseJoinColumns = {@JoinColumn(name="setor_id")})
	private List<Setor> setores;
	
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "usuario_permissao",
			joinColumns = {@JoinColumn(name="usuario_codigo")}, 
			inverseJoinColumns = {@JoinColumn(name="permissao_codigo")})
	private List<Permissao> permissoes;
	
}
