package com.condocam.condomanager.domain.dto;

import com.condocam.condomanager.domain.entities.VagaEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VagaDTO {
    private String descricao;
    private Long id_condominio;

    public VagaDTO(VagaEntity vagaEntity) {
        this.descricao = vagaEntity.getDescricao();
        this.id_condominio = vagaEntity.getCondominio().getId_condominio();
    }
}
