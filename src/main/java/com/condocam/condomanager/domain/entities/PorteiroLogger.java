package com.condocam.condomanager.domain.entities;

import com.condocam.condomanager.domain.enums.GaragePassDirection;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PorteiroLogger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_log;

    @ManyToOne
    @JoinColumn(name = "proprietario_id", nullable = false)
    private MoradorEntity proprietario;

    @ManyToOne
    @JoinColumn(name = "condominio_id", nullable = false)
    private CondominioEntity condominio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private VeiculoEntity veiculo;

    @Enumerated(EnumType.STRING)
    private GaragePassDirection direction;

    private String placa;
    private Long timestamp;
}

