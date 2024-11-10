package com.condocam.condomanager.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.condocam.condomanager.domain.entities.VagaEntity;

@Repository
public interface VagaRepository extends JpaRepository<VagaEntity, Long> {}
