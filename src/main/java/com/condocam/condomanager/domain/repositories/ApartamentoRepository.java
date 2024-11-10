package com.condocam.condomanager.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.condocam.condomanager.domain.entities.ApartamentoEntity;

@Repository
public interface ApartamentoRepository extends JpaRepository<ApartamentoEntity, Long> {
    
    @Query("SELECT a FROM ApartamentoEntity a WHERE a.numero = :numero AND a.pavimento.bloco.condominio.id_condominio = :idCondominio")
    ApartamentoEntity findByNumeroAndCondominioId(@Param("numero") String numero, @Param("idCondominio") Long idCondominio);
}
