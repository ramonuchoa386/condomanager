package com.condocam.condomanager.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.condocam.condomanager.domain.dto.response.MoradorResponseDTO;
import com.condocam.condomanager.domain.dto.response.VeiculoResponseDTO;
import com.condocam.condomanager.domain.entities.ApartamentoEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApartamentoDTO {
    private String numero;
    private int numQuartos;
    private double area;
    private Long id_pavimento;
    private Long id_apartamento;
    private List<MoradorResponseDTO> moradores;
    private List<VeiculoResponseDTO> veiculos;
    
    public ApartamentoDTO(ApartamentoEntity apartamento) {
        this.numero = apartamento.getNumero();
        this.numQuartos = apartamento.getNumQuartos();
        this.area = apartamento.getArea();
        this.id_pavimento = apartamento.getPavimento().getId_pavimento();
        this.id_apartamento = apartamento.getId_apartamento();
        this.moradores = apartamento.getMoradores().stream().map(MoradorResponseDTO::new).collect(Collectors.toList());
        this.veiculos = apartamento.getVeiculos().stream().map(VeiculoResponseDTO::new).collect(Collectors.toList());
    }
    
}
