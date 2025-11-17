package com.zenotime.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PontoRegistradoMessage {
    private Long registroId;
    private Long funcionarioId;
    private String funcionarioNome;
    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraSaida;
    private Double horasTrabalhadas;
    private Long projetoId;
    private String projetoNome;
}

