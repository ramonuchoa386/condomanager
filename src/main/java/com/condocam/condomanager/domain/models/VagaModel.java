package com.condocam.condomanager.domain.models;

import com.condocam.condomanager.domain.interfaces.Vaga;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VagaModel implements Vaga {
    private int quantidade;
    private String namePattern;
}