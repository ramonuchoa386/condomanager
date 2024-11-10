package com.condocam.condomanager.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.condocam.condomanager.domain.entities.BlocoEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlocoDTO {
    private int andares;
    private int apartamentosPorAndar;
    private String nome;
    private Long id_condominio;
    private Long id_bloco;
    private List<PavimentoDTO> pavimentos;

    public BlocoDTO(BlocoEntity blocoEntity) {
        this.nome = blocoEntity.getNome();
        this.andares = blocoEntity.getAndares();
        this.apartamentosPorAndar = blocoEntity.getApartamentosPorAndar();
        this.id_condominio = blocoEntity.getCondominio().getId_condominio();
        this.id_bloco = blocoEntity.getId_bloco();
        this.pavimentos = blocoEntity.getPavimentos().stream().map(PavimentoDTO::new).collect(Collectors.toList());
    }
}