package com.condocam.condomanager.domain.models;

import com.condocam.condomanager.domain.enums.GaragePassDirection;
import com.condocam.condomanager.domain.interfaces.Logger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoggerModel implements Logger {
    private String placa;
    private Long timestamp;
    private Long id_condominio;
    private GaragePassDirection direction;
}
