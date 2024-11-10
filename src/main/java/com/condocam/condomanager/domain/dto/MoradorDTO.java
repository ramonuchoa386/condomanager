package com.condocam.condomanager.domain.dto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.condocam.condomanager.domain.dto.response.ApartamentoResponseDTO;
import com.condocam.condomanager.domain.entities.MoradorEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoradorDTO {
    private String primeiro_nome;
    private String segundo_nome;
    private List<ApartamentoResponseDTO> apartamentos;
    private Long id_morador;
    private List<VeiculoDTO> veiculos; 
    
    public MoradorDTO(MoradorEntity morador) {
        this.primeiro_nome = morador.getPrimeiro_nome();
        this.segundo_nome = morador.getSegundo_nome();
        this.apartamentos = morador.getApartamentos().stream().map(ApartamentoResponseDTO::new).collect(Collectors.toList());
        this.id_morador = morador.getId_morador();
        this.veiculos = Optional.ofNullable(morador.getVeiculos()).orElse(Collections.emptyList()).stream().map(VeiculoDTO::new).collect(Collectors.toList());
    }
}
