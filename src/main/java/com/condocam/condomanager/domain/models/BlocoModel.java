package com.condocam.condomanager.domain.models;

import com.condocam.condomanager.domain.interfaces.Bloco;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlocoModel implements Bloco {
    private int andares;
    private int apartamentosPorAndar;
    private String nome;
}
