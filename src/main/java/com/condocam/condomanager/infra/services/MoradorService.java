package com.condocam.condomanager.infra.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.condocam.condomanager.domain.dto.MoradorDTO;
import com.condocam.condomanager.domain.entities.ApartamentoEntity;
import com.condocam.condomanager.domain.entities.MoradorEntity;
import com.condocam.condomanager.domain.repositories.MoradorRepository;
import com.condocam.condomanager.infra.config.classes.GenericService;
import com.condocam.condomanager.infra.config.classes.Pagination;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MoradorService extends GenericService<MoradorEntity, MoradorDTO, MoradorRepository> {

    @Autowired
    protected MoradorRepository moradorRepository;
    
    @Override
    protected MoradorDTO convertToDto(MoradorEntity entity) {
        return new MoradorDTO(entity);
    };

    public Pagination<MoradorDTO> listarPorCondominio(Long id_condominio, int page, int size) {
        log.info("id do condominio: " + id_condominio);
        Pageable pageable = PageRequest.of(page, size);
        Page<MoradorEntity> entityPage = moradorRepository.findByCondominio(id_condominio, pageable);
        List<MoradorDTO> dtos = entityPage.getContent()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new Pagination<>(dtos, entityPage.getNumber(), entityPage.getSize(), entityPage.getTotalElements(), entityPage.getTotalPages());
    }

    public Pagination<MoradorDTO> listarPorAdministradora(Long id_administradora, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MoradorEntity> entityPage = moradorRepository.findByAdministradora(id_administradora, pageable);
        List<MoradorDTO> dtos = entityPage.getContent()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new Pagination<>(dtos, entityPage.getNumber(), entityPage.getSize(), entityPage.getTotalElements(), entityPage.getTotalPages());
    }

    @Override
    public void deletar(Long id) {
        MoradorEntity moradorEntity = this.moradorRepository.findById(id).orElse(null);

        if(moradorEntity != null) {
            List<ApartamentoEntity> apartamentos = moradorEntity.getApartamentos();

            for(int i = 0; i < apartamentos.size(); i++) {
                log.info("entrou no loop");
                apartamentos.get(i).removeMorador(moradorEntity);
            }
            log.info("saiu do loop");

            this.moradorRepository.deleteById(id);
        }
    }

    @Override
    public void bulkDelete(List<Long> ids) {
        log.info("bulk delete moradores service " + ids);
        for(int index = 0; index < ids.size(); index++) {
            MoradorEntity moradorEntity = this.moradorRepository.findById(ids.get(index)).orElse(null);

            if(moradorEntity != null) {
                List<ApartamentoEntity> apartamentos = moradorEntity.getApartamentos();

                for(int i = 0; i < apartamentos.size(); i++) {
                    log.info("entrou no loop");
                    apartamentos.get(i).removeMorador(moradorEntity);
                }
                log.info("saiu do loop");

                this.moradorRepository.deleteById(ids.get(index));
            }
        }

        // super.bulkDelete(ids);
    }
}
