package com.project.Blackbelt.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.Blackbelt.Model.Users;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<Users, Long>{

	@Query("select u from Users u where u.login = ?1 ")
	public Users findByUsername(String login);
	
	@Query("select u from Users u where u.tokenRecuperacaoSenha = ?1 ")
	public Users findByTokenRecuperacaoSenha(String tokenRecuperacaoSenha);
	

}
