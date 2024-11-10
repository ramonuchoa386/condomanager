package com.condocam.condomanager.domain.entities;

import lombok.Data;
import lombok.AllArgsConstructor;


@Data
@AllArgsConstructor
public class ErrorResponseEntity {
    private String message;
}