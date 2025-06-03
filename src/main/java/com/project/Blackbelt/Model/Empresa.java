package com.project.Blackbelt.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Empresa implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "cnpj",nullable = false,length = 17)
	private String cnpj;
	@Column(name = "nome",length = 100)
	private String nome;
	@Column(name = "razaosocial",length = 100)
	private String razaosocial;
	
	@OneToMany(mappedBy = "empresa",orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Users> usuarios = new ArrayList<Users>();
	
	@OneToMany(mappedBy = "empresa",orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Documento> documento = new ArrayList<Documento>();
	
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getRazaosocial() {
		return razaosocial;
	}
	public void setRazaosocial(String razaosocial) {
		this.razaosocial = razaosocial;
	}
	
	
}
