package com.condocam.condomanager.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.condocam.condomanager.domain.entities.PavimentoEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PavimentoDTO {
    private int numero;
    private Long id_bloco;
    private List<ApartamentoDTO> apartamentos;

    public PavimentoDTO(PavimentoEntity pavimento) {
        this.id_bloco = pavimento.getBloco().getId_bloco();
        this.numero = pavimento.getNumero();
        this.apartamentos = pavimento.getApartamentos().stream().map(ApartamentoDTO::new).collect(Collectors.toList());
    }
}
