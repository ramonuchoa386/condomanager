package com.condocam.condomanager.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PavimentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pavimento;

    private int numero;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bloco")
    private BlocoEntity bloco;

    @JsonManagedReference
    @OneToMany(mappedBy = "pavimento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ApartamentoEntity> apartamentos;

    public void addApartamento(ApartamentoEntity apartamento) {
        apartamentos.add(apartamento);
        apartamento.setPavimento(this);
    }

    public void removeApartamento(ApartamentoEntity apartamento) {
        apartamentos.remove(apartamento);
        apartamento.setPavimento(null);
    }
}

