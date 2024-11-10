package com.condocam.condomanager.domain.models;

import com.condocam.condomanager.domain.dto.VeiculoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoradorModel {
    private String unidade;
    private String primeiro_nome;
    private String segundo_nome;
    private VeiculoDTO veiculo;
}
