package com.condocam.condomanager.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PipelineResponseDTO {
    private String message;
    private int code;
}
