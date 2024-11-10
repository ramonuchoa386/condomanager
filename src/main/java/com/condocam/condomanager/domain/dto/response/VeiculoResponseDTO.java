package com.condocam.condomanager.domain.dto.response;

import com.condocam.condomanager.domain.entities.VeiculoEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoResponseDTO {
    private String marca;
    private String modelo;
    private String placa;
    private Long id_veiculo;

    public VeiculoResponseDTO(VeiculoEntity veiculoEntity) {
        this.marca = veiculoEntity.getMarca();
        this.modelo = veiculoEntity.getModelo();
        this.placa = veiculoEntity.getPlaca();
        this.id_veiculo = veiculoEntity.getId_veiculo();
    }
}

