package com.condocam.condomanager.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaComumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_areacomum;

    private String nome;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_condominio")
    private CondominioEntity condominio;
}

