package com.condocam.condomanager.infra.services;

import org.springframework.stereotype.Service;

import com.condocam.condomanager.domain.dto.BlocoDTO;
import com.condocam.condomanager.domain.entities.BlocoEntity;
import com.condocam.condomanager.domain.repositories.BlocoRepository;
import com.condocam.condomanager.infra.config.classes.GenericService;

@Service
public class BlocoService extends GenericService<BlocoEntity, BlocoDTO, BlocoRepository> {
    
    @Override
    protected BlocoDTO convertToDto(BlocoEntity entity) {
        return new BlocoDTO(entity);
    }
}


