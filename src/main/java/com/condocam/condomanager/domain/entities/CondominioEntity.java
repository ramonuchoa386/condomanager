package com.condocam.condomanager.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CondominioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_condominio;

    @NotNull(message = "O nome do condomínio não pode ser nulo.")
    private String nome;

    private String endereco;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_administradora", nullable = false)
    @NotNull(message = "O condomínio precisa ser associado a uma administradora.")
    private AdministradoraEntity administradora;

    @JsonManagedReference
    @OneToMany(mappedBy = "condominio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AreaComumEntity> areasComuns;

    @JsonManagedReference
    @OneToMany(mappedBy = "condominio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BlocoEntity> blocos;

    @JsonManagedReference
    @OneToMany(mappedBy = "condominio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VagaEntity> vagas;

    public void addAreaComum(AreaComumEntity areaComum) {
        areasComuns.add(areaComum);
        areaComum.setCondominio(this);
    }

    public void removeAreaComum(AreaComumEntity areaComum) {
        areasComuns.remove(areaComum);
        areaComum.setCondominio(null);
    }

    public void addBloco(BlocoEntity bloco) {
        blocos.add(bloco);
        bloco.setCondominio(this);
    }

    public void removeBloco(BlocoEntity bloco) {
        blocos.remove(bloco);
        bloco.setCondominio(null);
    }

    public void addVaga(VagaEntity vaga) {
        vagas.add(vaga);
        vaga.setCondominio(this);
    }

    public void removeVaga(VagaEntity vaga) {
        vagas.remove(vaga);
        vaga.setCondominio(null);
    }
}
