package com.condocam.condomanager.domain.dto;

import com.condocam.condomanager.domain.entities.PorteiroLogger;
import com.condocam.condomanager.domain.enums.GaragePassDirection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PorteiroLoggerDTO {
    private String placa;
    private Long id_condominio;
    private GaragePassDirection direction;
    private Long timestamp;
    private Long id_log;
    private MoradorDTO morador;
    private VeiculoDTO veiculo;

    public PorteiroLoggerDTO(PorteiroLogger log) {
        this.placa = log.getPlaca();
        this.id_condominio = log.getCondominio().getId_condominio();
        this.direction = log.getDirection();
        this.timestamp = log.getTimestamp();
        this.id_log = log.getId_log();
        this.morador = new MoradorDTO(log.getProprietario());
        this.veiculo = new VeiculoDTO(log.getVeiculo());
    }
}
