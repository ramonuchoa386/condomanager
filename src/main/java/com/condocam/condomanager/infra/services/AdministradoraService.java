package com.condocam.condomanager.infra.services;

import org.springframework.stereotype.Service;

import com.condocam.condomanager.domain.dto.AdministradoraDTO;
import com.condocam.condomanager.domain.entities.AdministradoraEntity;
import com.condocam.condomanager.domain.repositories.AdministradoraRepository;
import com.condocam.condomanager.infra.config.classes.GenericService;

@Service
public class AdministradoraService extends GenericService<AdministradoraEntity, AdministradoraDTO, AdministradoraRepository> {

    @Override
    public AdministradoraDTO convertToDto(AdministradoraEntity entity) {
        return new AdministradoraDTO(entity);
    }
}

