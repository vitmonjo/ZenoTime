package com.zenotime.dto;

import com.zenotime.entity.RegistroPonto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RegistroPontoResponse {
    private Long id;
    private Long funcionarioId;
    private String funcionarioNome;
    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraSaida;
    private Double horasTrabalhadas;
    private Long projetoId;
    private String projetoNome;
    private String observacoes;
    private RegistroPonto.TipoRegistro tipo;
}

