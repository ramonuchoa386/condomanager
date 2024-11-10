package com.condocam.condomanager.infra.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.condocam.condomanager.domain.dto.ApartamentoDTO;
import com.condocam.condomanager.domain.entities.ApartamentoEntity;
import com.condocam.condomanager.domain.entities.BlocoEntity;
import com.condocam.condomanager.domain.entities.PavimentoEntity;
import com.condocam.condomanager.domain.repositories.ApartamentoRepository;
import com.condocam.condomanager.infra.config.classes.GenericService;

import lombok.NonNull;

@Service
public class ApartamentoService extends GenericService<ApartamentoEntity, ApartamentoDTO, ApartamentoRepository> {

    @Autowired
    private ApartamentoRepository apartamentoRepository;
    
    @Override
    protected ApartamentoDTO convertToDto(ApartamentoEntity entity) {
        return new ApartamentoDTO(entity);
    }

    @Override
    public ApartamentoDTO salvar(@NonNull ApartamentoEntity apartamento) throws Exception {
        PavimentoEntity pavimento = apartamento.getPavimento();
        BlocoEntity bloco = pavimento.getBloco();

        int maxApartamentos = bloco.getApartamentosPorAndar();
        int numApartamentos = pavimento.getApartamentos().size();

        if (numApartamentos == maxApartamentos) {
            throw new Exception("Máximo número de apartamentos atingido neste pavimento.");
        };

        ApartamentoEntity apartamentoGravado = apartamentoRepository.save(apartamento);

        return convertToDto(apartamentoGravado);
    }
}

