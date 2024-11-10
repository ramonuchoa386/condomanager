package com.condocam.condomanager.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministradoraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_administradora;

    private String nome;

    @JsonManagedReference
    @OneToMany(mappedBy = "administradora", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CondominioEntity> condominios;

    public void addCondominio(CondominioEntity condominio) {
        condominios.add(condominio);
        condominio.setAdministradora(this);
    }

    public void removeCondominio(CondominioEntity condominio) {
        condominios.remove(condominio);
        condominio.setAdministradora(null);
    }
}
