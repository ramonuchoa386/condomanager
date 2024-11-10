package com.condocam.condomanager.domain.interfaces;

import com.condocam.condomanager.domain.enums.GaragePassDirection;

public interface Logger {
    public String placa = "";
    public Long timestamps = 0L;
    public Long id_condominio = 0L;
    public GaragePassDirection direction = GaragePassDirection.ENTRADA;
}
