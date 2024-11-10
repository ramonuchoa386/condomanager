package com.condocam.condomanager.infra.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.condocam.condomanager.domain.dto.CondominioDTO;
import com.condocam.condomanager.domain.entities.CondominioEntity;
import com.condocam.condomanager.domain.repositories.CondominioRepository;
import com.condocam.condomanager.infra.config.classes.GenericService;
import com.condocam.condomanager.infra.config.classes.Pagination;

@Service
public class CondominioService extends GenericService<CondominioEntity, CondominioDTO, CondominioRepository> {

    @Override
    protected CondominioDTO convertToDto(CondominioEntity entity) {
        return new CondominioDTO(entity);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'master')")
    public Pagination<CondominioDTO> getCondominiosByIdAdministradora(Long idAdministradora, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CondominioEntity> entityPage = repository.findByIdAdministradora(idAdministradora, pageable);
        List<CondominioDTO> dtos = entityPage.getContent()
                        .stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList());

        return new Pagination<>(dtos, entityPage.getNumber(), entityPage.getSize(), entityPage.getTotalElements(), entityPage.getTotalPages());
    }
}
