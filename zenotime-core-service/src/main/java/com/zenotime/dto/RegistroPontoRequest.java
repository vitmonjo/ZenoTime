package com.zenotime.dto;

import com.zenotime.entity.RegistroPonto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegistroPontoRequest {
    @NotNull(message = "Data/hora de entrada é obrigatória")
    private LocalDateTime dataHoraEntrada;
    
    private LocalDateTime dataHoraSaida;
    
    private Long projetoId;
    
    private String observacoes;
    
    private RegistroPonto.TipoRegistro tipo = RegistroPonto.TipoRegistro.NORMAL;
}

