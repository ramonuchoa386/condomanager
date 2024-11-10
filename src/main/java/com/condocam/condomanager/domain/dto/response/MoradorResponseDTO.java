package com.condocam.condomanager.domain.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.condocam.condomanager.domain.entities.MoradorEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoradorResponseDTO {
    private String primeiro_nome;
    private String segundo_nome;
    private Long id_morador;
    private List<Long> apartamentos_ids;
    private List<VeiculoResponseDTO> veiculos;

    public MoradorResponseDTO(MoradorEntity morador) {
        this.primeiro_nome = morador.getPrimeiro_nome();
        this.segundo_nome = morador.getSegundo_nome();
        this.id_morador = morador.getId_morador();
        this.apartamentos_ids = morador.getApartamentosIds();
        this.veiculos = morador.getVeiculos().stream().map(VeiculoResponseDTO::new).collect(Collectors.toList());
    }
}