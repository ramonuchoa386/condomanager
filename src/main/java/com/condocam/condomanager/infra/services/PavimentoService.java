package com.condocam.condomanager.infra.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.condocam.condomanager.domain.dto.PavimentoDTO;
import com.condocam.condomanager.domain.entities.PavimentoEntity;
import com.condocam.condomanager.domain.repositories.PavimentoRepository;
import com.condocam.condomanager.infra.config.classes.GenericService;

import lombok.NonNull;

@Service
public class PavimentoService extends GenericService<PavimentoEntity, PavimentoDTO, PavimentoRepository> {

    @Autowired
    private PavimentoRepository pavimentoRepository;
    
    @Override
    protected PavimentoDTO convertToDto(PavimentoEntity entity) {
        return new PavimentoDTO(entity);
    }
    
    @Override
    public PavimentoDTO salvar(@NonNull PavimentoEntity pavimento) throws Exception {
        int maxPavimentos = pavimento.getBloco().getAndares();
        int pavimentosNum = pavimento.getBloco().getPavimentos().size();

        if (pavimentosNum == maxPavimentos) {
            throw new Exception("Máximo número de pavimentos atingido.");
        };

        PavimentoEntity pavimentoGravado = pavimentoRepository.save(pavimento);

        return convertToDto(pavimentoGravado);
    }
}


