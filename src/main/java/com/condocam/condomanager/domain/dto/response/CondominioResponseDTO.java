package com.condocam.condomanager.domain.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.condocam.condomanager.domain.dto.BlocoDTO;
import com.condocam.condomanager.domain.entities.ApartamentoEntity;
import com.condocam.condomanager.domain.entities.AreaComumEntity;
import com.condocam.condomanager.domain.entities.BlocoEntity;
import com.condocam.condomanager.domain.entities.CondominioEntity;
import com.condocam.condomanager.domain.entities.PavimentoEntity;
import com.condocam.condomanager.domain.entities.VagaEntity;
import com.condocam.condomanager.domain.entities.VeiculoEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CondominioResponseDTO {
    private Long id_condominio;
    private String nome;
    private String endereco;
    private List<AreaComumEntity> areas_comuns;
    private List<BlocoDTO> blocos;
    private List<VagaEntity> vagas;
    private List<VeiculoResponseDTO> veiculos;
    private int quantidade_de_moradores;

    public CondominioResponseDTO(CondominioEntity entity) {
        this.id_condominio = entity.getId_condominio();
        this.nome = entity.getNome();
        this.endereco = entity.getEndereco();
        this.areas_comuns = entity.getAreasComuns();
        this.blocos = entity.getBlocos().stream().map(BlocoDTO::new).collect(Collectors.toList());
        this.vagas = entity.getVagas();

        int totalMoradores = 0;
        List<VeiculoEntity> listaVeiculos = new ArrayList<VeiculoEntity>();

        for (BlocoEntity bloco : entity.getBlocos()) {
            for (PavimentoEntity pavimento : bloco.getPavimentos()) {
                for (ApartamentoEntity apartamento : pavimento.getApartamentos()) {
                    totalMoradores += apartamento.getMoradores().size();
                    listaVeiculos.addAll(apartamento.getVeiculos());
                }
            }
        }

        this.quantidade_de_moradores = totalMoradores;
        this.veiculos = listaVeiculos.stream().map(VeiculoResponseDTO::new).collect(Collectors.toList());
    }
}
