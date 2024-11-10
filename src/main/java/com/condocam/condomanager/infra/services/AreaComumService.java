package com.condocam.condomanager.infra.services;

import org.springframework.stereotype.Service;

import com.condocam.condomanager.domain.dto.AreaComumDTO;
import com.condocam.condomanager.domain.entities.AreaComumEntity;
import com.condocam.condomanager.domain.repositories.AreaComumRepository;
import com.condocam.condomanager.infra.config.classes.GenericService;

@Service
public class AreaComumService extends GenericService<AreaComumEntity, AreaComumDTO, AreaComumRepository> {
    
    @Override
    protected AreaComumDTO convertToDto(AreaComumEntity entity) {
        return new AreaComumDTO(entity);
    };
}


