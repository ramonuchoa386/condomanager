package com.condocam.condomanager.domain.dto.response;

import com.condocam.condomanager.domain.entities.ApartamentoEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApartamentoResponseDTO {
    private String numero;
    private int numQuartos;
    private double area;
    private Long id_apartamento;

    public ApartamentoResponseDTO(ApartamentoEntity apartamento) {
        this.numero = apartamento.getNumero();
        this.numQuartos = apartamento.getNumQuartos();
        this.area = apartamento.getArea();
        this.id_apartamento = apartamento.getId_apartamento();
    }
}