package com.condocam.condomanager.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.condocam.condomanager.domain.entities.MoradorEntity;

@Repository
public interface MoradorRepository extends JpaRepository<MoradorEntity, Long> {

    @Query("SELECT m FROM MoradorEntity m " +
           "LEFT JOIN m.apartamentos a " +
           "LEFT JOIN a.pavimento p " +
           "LEFT JOIN p.bloco b " +
           "LEFT JOIN b.condominio c " +
           "WHERE c.id = :idCondominio")
    Page<MoradorEntity> findByCondominio(@Param("idCondominio") Long idCondominio,
                                                          Pageable pageable);

    @Query("SELECT m FROM MoradorEntity m " +
        "JOIN m.apartamentos a " +
        "JOIN a.pavimento p " +
        "JOIN p.bloco b " +
        "JOIN b.condominio c " +
        "WHERE c.administradora.id = :idAdministradora")
    Page<MoradorEntity> findByAdministradora(@Param("idAdministradora") Long idAdministradora, Pageable pageable);
}