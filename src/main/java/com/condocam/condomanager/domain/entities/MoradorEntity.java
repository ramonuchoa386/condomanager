package com.condocam.condomanager.domain.entities;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MoradorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_morador;
    private String primeiro_nome;
    private String segundo_nome;

    @JsonManagedReference
    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VeiculoEntity> veiculos;

    @JsonBackReference
    @ManyToMany(mappedBy = "moradores", fetch = FetchType.LAZY)
    private List<ApartamentoEntity> apartamentos;

    public void addApartamento(ApartamentoEntity apartamento) {
        apartamentos.add(apartamento);
        apartamento.getMoradores().add(this);
    }

    public void removeApartamento(ApartamentoEntity apartamento) {
        apartamentos.remove(apartamento);
        apartamento.getMoradores().remove(this);
    }

    public List<Long> getApartamentosIds() {
        return this.apartamentos.stream()
                .map(ApartamentoEntity::getId_apartamento)
                .collect(Collectors.toList());
    }

    public boolean allowSindico(Long paramId) {
        for (ApartamentoEntity apartamento : apartamentos) {
            PavimentoEntity pavimento = apartamento.getPavimento();

            if (pavimento != null) {
                BlocoEntity bloco = pavimento.getBloco();

                if (bloco != null) {
                    CondominioEntity condominio = bloco.getCondominio();

                    return condominio != null && paramId.equals(condominio.getId_condominio());
                }
            }
        }

        return false;
    }

    public boolean allowAdmin(Long paramId) {
        for (ApartamentoEntity apartamento : apartamentos) {
            PavimentoEntity pavimento = apartamento.getPavimento();

            if (pavimento != null) {
                BlocoEntity bloco = pavimento.getBloco();

                if (bloco != null) {
                    CondominioEntity condominio = bloco.getCondominio();

                    if (condominio != null) {
                        AdministradoraEntity administradora = condominio.getAdministradora();
    
                        return administradora != null && paramId.equals(administradora.getId_administradora());
                    }
                }
            }
        }

        return false;
    }
}
