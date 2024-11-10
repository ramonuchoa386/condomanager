package com.condocam.condomanager.domain.entities;

import lombok.*;
import jakarta.persistence.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlocoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_bloco;

    private int andares;
    private int apartamentosPorAndar;
    private String nome;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_condominio", nullable = false)
    private CondominioEntity condominio;

    @JsonManagedReference
    @OneToMany(mappedBy = "bloco", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PavimentoEntity> pavimentos;

    public void addPavimento(PavimentoEntity pavimento) {
        pavimentos.add(pavimento);
        pavimento.setBloco(this);
    }

    public void removePavimento(PavimentoEntity pavimento) {
        pavimentos.remove(pavimento);
        pavimento.setBloco(null);
    }
}
