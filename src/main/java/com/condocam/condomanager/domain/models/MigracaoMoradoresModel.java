package com.condocam.condomanager.domain.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MigracaoMoradoresModel {
    private long id_condominio;
    private List<MoradorModel> moradores;
}
