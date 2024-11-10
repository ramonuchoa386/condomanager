package com.condocam.condomanager.domain.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApartamentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_apartamento;

    private String numero;

    private boolean alugado;

    private String nomeProprietario;

    private int numQuartos;
    private double area;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pavimento")
    private PavimentoEntity pavimento;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "apartamento_morador",
        joinColumns = @JoinColumn(name = "id_apartamento"),
        inverseJoinColumns = @JoinColumn(name = "id_morador")
    )
    private List<MoradorEntity> moradores;

    @OneToMany(mappedBy = "apartamento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VeiculoEntity> veiculos;

    public void addMorador(MoradorEntity morador) {
        moradores.add(morador);
        morador.getApartamentos().add(this);
    }

    public void removeMorador(MoradorEntity morador) {
        moradores.remove(morador);
        morador.getApartamentos().remove(this);
    }

    public void addVeiculo(VeiculoEntity veiculo) {
        veiculos.add(veiculo);
        veiculo.setApartamento(this);
    }

    public void removeVeiculo(VeiculoEntity veiculo) {
        veiculos.remove(veiculo);
        veiculo.setApartamento(null);
    }

    public Long getRelatedAdministradora() {
        return this.pavimento.getBloco().getCondominio().getAdministradora().getId_administradora();
    }

    public Long getRelatedCondominio() {
        return this.pavimento.getBloco().getCondominio().getId_condominio();
    }
}