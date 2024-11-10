package com.condocam.condomanager.infra.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.condocam.condomanager.domain.dto.PorteiroLoggerDTO;
import com.condocam.condomanager.domain.entities.PorteiroLogger;
import com.condocam.condomanager.domain.repositories.RegistrosRepository;
import com.condocam.condomanager.infra.config.classes.GenericService;
import com.condocam.condomanager.infra.config.classes.Pagination;

@Service
public class RegistrosService extends GenericService<PorteiroLogger, PorteiroLoggerDTO, RegistrosRepository> {

    @Autowired
    private RegistrosRepository registrosRepository;

    @Override
    protected PorteiroLoggerDTO convertToDto(PorteiroLogger entity) {
        return new PorteiroLoggerDTO(entity);
    }

    public Pagination<PorteiroLoggerDTO> listarPorCondominio(Long id_condominio, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PorteiroLogger> entityPage = this.registrosRepository.findByCondominioId(id_condominio, pageable);
        List<PorteiroLoggerDTO> dtos = entityPage.getContent().stream().map(this::convertToDto).collect(Collectors.toList());

        return new Pagination<>(dtos, entityPage.getNumber(), entityPage.getSize(), entityPage.getTotalElements(), entityPage.getTotalPages());
    }
}
