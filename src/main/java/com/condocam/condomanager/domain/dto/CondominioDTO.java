package com.condocam.condomanager.domain.dto;

import com.condocam.condomanager.domain.entities.CondominioEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CondominioDTO {
    private Long id_condominio;
    private String nome;
    private String endereco;
    private Long id_administradora;

    public CondominioDTO(CondominioEntity condominioEntity) {
        this.nome = condominioEntity.getNome();
        this.endereco = condominioEntity.getEndereco();
        this.id_administradora = condominioEntity.getAdministradora().getId_administradora();
        this.id_condominio = condominioEntity.getId_condominio();
    }
}


