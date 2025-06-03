package com.project.Blackbelt.Model;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;


/**
 * Classe Users da aplicação Blackbelt .
 * <p>
 * Data de criação: 02-04-2025
 * </p>
 * @author Poopstoop1 - Paulo Daniel
 * @version 1.0
 * @since Java 21
 * 
 * */

@Entity
public class Users implements UserDetails {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
	private Long id;
	@Column(name = "email",nullable = false,length = 100)
	private String login;
	@Column(name = "password",nullable = false,length = 16)
	private String password;
	@Column(nullable = false,length = 100)
	private String nome;
	
	@Column(name = "cargo",nullable = false,length = 40)
	private String cargo;
	@Column(name = "permissao",nullable = false)
	private String permissao;
	@Column(name = "tokenRecuperacaoSenha", nullable = true)
	private String tokenRecuperacaoSenha;
	@Column(name = "tokenExpiracao", nullable = true)
    private LocalDateTime tokenExpiracao;
	
	@ManyToOne
	@JoinColumn(name = "cnpj_empresa", referencedColumnName = "cnpj")
	private Empresa empresa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}
	
	public String getTokenRecuperacaoSenha() {
		return tokenRecuperacaoSenha;
	}

	public void setTokenRecuperacaoSenha(String tokenRecuperacaoSenha) {
		this.tokenRecuperacaoSenha = tokenRecuperacaoSenha;
	}

	public LocalDateTime getTokenExpiracao() {
		return tokenExpiracao;
	}

	public void setTokenExpiracao(LocalDateTime tokenExpiracao) {
		this.tokenExpiracao = tokenExpiracao;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getPermissao() {
		return permissao;
	}

	public void setPermissao(String permissao) {
		this.permissao = permissao;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    String role = permissao.equalsIgnoreCase("admin")
	                  ? "ROLE_MANAGER" 
	                  : "ROLE_USER";
	    return Collections.singletonList(new SimpleGrantedAuthority(role));
	}
	
	@Override
	public String getUsername() {
		return login;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	public String getCnpjEmpresa() {
	    return empresa != null ? empresa.getCnpj() : null;
	}
}
