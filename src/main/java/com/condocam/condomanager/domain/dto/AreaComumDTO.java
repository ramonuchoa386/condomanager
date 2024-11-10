package com.condocam.condomanager.domain.dto;

import com.condocam.condomanager.domain.entities.AreaComumEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AreaComumDTO {
    private String nome;
    private Long id_condominio;

    public AreaComumDTO(AreaComumEntity areaComumEntity) {
        this.nome = areaComumEntity.getNome();
        this.id_condominio = areaComumEntity.getCondominio().getId_condominio();
    }
}


