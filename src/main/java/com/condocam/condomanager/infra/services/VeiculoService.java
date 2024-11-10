package com.condocam.condomanager.infra.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.condocam.condomanager.domain.dto.VeiculoDTO;
import com.condocam.condomanager.domain.entities.VeiculoEntity;
import com.condocam.condomanager.domain.repositories.VeiculosRepository;
import com.condocam.condomanager.infra.config.classes.GenericService;

@Service
public class VeiculoService extends GenericService<VeiculoEntity, VeiculoDTO, VeiculosRepository> {

    @Autowired
    protected VeiculosRepository veiculosRepository;

    @Override
    protected VeiculoDTO convertToDto(VeiculoEntity entity) {
        return new VeiculoDTO(entity);
    }

    public VeiculoEntity findByPlaca(String placa) {
        return veiculosRepository.findByPlaca(placa).orElse(null);
    }
}
