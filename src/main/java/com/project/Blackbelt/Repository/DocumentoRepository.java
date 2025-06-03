package com.project.Blackbelt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.Blackbelt.Model.Documento;
import com.project.Blackbelt.Model.Empresa;


@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
	
	 @Query("SELECT d FROM Documento d JOIN empresa f WHERE f.cnpj = :cnpj")
	    List<Documento> findDocumentoByCnpj(@Param("cnpj") String cnpj);
	 
	 @Query("SELECT COUNT(d) > 0 FROM Documento d WHERE " +
		       "d.data = :data AND " +
		       "d.ativo = :ativo AND " +
		       "d.ip = :ip AND " +
		       "d.cves = :cves AND " +
		       "d.criticidade = :criticidade AND " +
		       "d.recomendacao_correcao = :recomendacao_correcao AND " +
		       "d.responsavel = :responsavel AND " +
		       "d.empresa = :empresa")
		boolean existsDocumentoCompleto(@Param("data") String data,
		                               @Param("ativo") String ativo,
		                               @Param("ip") String ip,
		                               @Param("cves") String cves,
		                               @Param("criticidade") String criticidade,
		                               @Param("recomendacao_correcao") String recomendacao_correcao,
		                               @Param("responsavel") String responsavel, 
		                               @Param("empresa") Empresa empresa);
	 
	 @Query("SELECT d FROM Documento d WHERE " +
		       "(COALESCE(:busca, '') = '' OR " +
		       "LOWER(d.ativo) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
		       "LOWER(d.ip) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
		       "LOWER(d.cves) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
		       "LOWER(d.responsavel) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
		       "LOWER(d.data) LIKE LOWER(CONCAT('%', :busca, '%'))) AND " + // Adicionado busca por data
		       "(COALESCE(:criticidade, '') = '' OR d.criticidade = :criticidade) AND " +
		       "(COALESCE(:status, '') = '' OR d.status = :status)")
		List<Documento> filtrarDocumentos(
		    @Param("busca") String busca,
		    @Param("criticidade") String criticidade,
		    @Param("status") String status
		);
}
