package com.condocam.condomanager.infra.services;

import org.springframework.stereotype.Service;

import com.condocam.condomanager.domain.dto.VagaDTO;
import com.condocam.condomanager.domain.entities.VagaEntity;
import com.condocam.condomanager.domain.repositories.VagaRepository;
import com.condocam.condomanager.infra.config.classes.GenericService;

@Service
public class VagaService extends GenericService<VagaEntity, VagaDTO, VagaRepository> {
    
    @Override
    protected VagaDTO convertToDto(VagaEntity entity) {
        return new VagaDTO(entity);
    }
}


