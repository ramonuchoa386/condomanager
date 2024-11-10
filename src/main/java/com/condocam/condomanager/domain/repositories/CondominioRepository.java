package com.condocam.condomanager.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.condocam.condomanager.domain.entities.CondominioEntity;

@Repository
public interface CondominioRepository extends JpaRepository<CondominioEntity, Long> {

    @Query("SELECT c FROM CondominioEntity c WHERE c.administradora.id = :idAdministradora")
    Page<CondominioEntity> findByIdAdministradora(@Param("idAdministradora")Long  idAdministradora, Pageable pageable);
}
