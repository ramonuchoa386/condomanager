package com.condocam.condomanager.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.condocam.condomanager.domain.entities.AdministradoraEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdministradoraDTO {
    private Long id_administradora;
    private String nome;
    private List<CondominioDTO> condominios;

    public AdministradoraDTO(AdministradoraEntity administradoraEntity) {
        this.id_administradora = administradoraEntity.getId_administradora();
        this.nome = administradoraEntity.getNome();

        if(administradoraEntity.getCondominios() != null) {
            this.condominios = administradoraEntity.getCondominios().stream().map(CondominioDTO::new).collect(Collectors.toList());
        }
    }
}


