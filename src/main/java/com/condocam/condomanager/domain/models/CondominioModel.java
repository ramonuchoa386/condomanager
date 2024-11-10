package com.condocam.condomanager.domain.models;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CondominioModel {
    private Long id_administradora;
    private String nome;
    private String endereco;
    private Set<String> listaAreasComum;
    private List<BlocoModel> blocos;
    private VagaModel vagas;

    // public CondominioModel(CondominioModel condominio) {
    //     this.id_administradora = condominio.getId_administradora();
    //     this.nome = condominio.getNome();
    //     this.endereco = condominio.getEndereco();
    //     this.listaAreasComum = condominio.getListaAreasComum();
    //     this.blocos = condominio.getBlocos();
    //     this.vagas = condominio.getVagas();
    // }
}
