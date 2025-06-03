package com.project.Blackbelt.Model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

/**
 * Classe Documento da aplicação Blackbelt para injetar dados da planilha.
 * <p>
 * Data de criação: 02-09-2025
 * </p>
 * @author Poopstoop1 - Paulo Daniel
 * @version 1.0
 * @since Java 21
 * 
 * */
@Entity
public class Documento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doc_seq")
    @SequenceGenerator(name = "doc_seq", sequenceName = "doc_seq", allocationSize = 1)
	private long id;
	@Column(name = "data",nullable = true)
	private String data;
	@Column(nullable = false)
	private String ativo;
	@Column(nullable = true)
	private String ip;
	@Column(nullable = true)
	private String cves;
	@Column(nullable = true)
	private String criticidade;
	@Column(nullable = true)
	private String recomendacao_correcao;
	@Column(nullable = true)
	private String responsavel;
	@Column(nullable = true)
	private String status;
	@Column(nullable = true)
	private String data_correcao;
	@Column(nullable = true)
	private String observacao;
	@Column(nullable = true)
	private String correcao;
	@ManyToOne
	@JoinColumn(name = "cnpj_filial", referencedColumnName = "cnpj")
	private Empresa empresa;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getAtivo() {
		return ativo;
	}
	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCves() {
		return cves;
	}
	public void setCves(String cves) {
		this.cves = cves;
	}
	public String getCriticidade() {
		return criticidade;
	}
	public void setCriticidade(String criticidade) {
		this.criticidade = criticidade;
	}
	public String getRecomendacao_correcao() {
		return recomendacao_correcao;
	}
	public void setRecomendacao_correcao(String recomendacao_correcao) {
		this.recomendacao_correcao = recomendacao_correcao;
	}
	public String getResponsavel() {
		return responsavel;
	}
	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getData_correcao() {
		return data_correcao;
	}
	public void setData_correcao(String data_correcao) {
		this.data_correcao = data_correcao;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public String getCorrecao() {
		return correcao;
	}
	public void setCorrecao(String correcao) {
		this.correcao = correcao;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	
}
