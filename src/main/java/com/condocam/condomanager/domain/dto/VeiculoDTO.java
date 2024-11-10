package com.condocam.condomanager.domain.dto;

import com.condocam.condomanager.domain.entities.VeiculoEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoDTO {
    private String marca;
    private String modelo;
    private String placa;
    private Long id_morador;
    private Long id_veiculo;
    private ApartamentoDTO apartamento;

    public VeiculoDTO(VeiculoEntity veiculoEntity) {
        this.marca = veiculoEntity.getMarca();
        this.modelo = veiculoEntity.getModelo();
        this.placa = veiculoEntity.getPlaca();
        this.id_morador = veiculoEntity.getProprietario().getId_morador();
        this.id_veiculo = veiculoEntity.getId_veiculo();
        this.apartamento = new ApartamentoDTO(veiculoEntity.getApartamento());
    }
}

