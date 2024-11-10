package com.condocam.condomanager.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.condocam.condomanager.domain.entities.PorteiroLogger;

@Repository
public interface RegistrosRepository extends JpaRepository<PorteiroLogger, Long> {

    @Query("SELECT p FROM PorteiroLogger p WHERE p.condominio.id_condominio = :id_condominio ORDER BY p.timestamp DESC")
    Page<PorteiroLogger> findByCondominioId(@Param("id_condominio") Long id_condominio, Pageable pageable);
}

