package com.project.Blackbelt.Repository;



import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import com.project.Blackbelt.Model.Empresa;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface EmpresaRepository extends CrudRepository<Empresa, String>{
	Empresa findByNome(String nome);
	
	
}
