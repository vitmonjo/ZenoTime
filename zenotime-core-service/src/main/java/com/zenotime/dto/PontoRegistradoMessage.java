package com.zenotime.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PontoRegistradoMessage {
    private String registroId;
    private String funcionarioId;
    private String funcionarioNome;
    private String dataHoraEntrada;
    private String dataHoraSaida;
    private String horasTrabalhadas;
    private String projetoId;
    private String projetoNome;

    // Método estático para criar mensagem a partir de LocalDateTime
    public static PontoRegistradoMessage create(Long registroId, Long funcionarioId, String funcionarioNome,
                                               LocalDateTime dataHoraEntrada, LocalDateTime dataHoraSaida,
                                               Double horasTrabalhadas, Long projetoId, String projetoNome) {
        return new PontoRegistradoMessage(
            registroId != null ? registroId.toString() : null,
            funcionarioId != null ? funcionarioId.toString() : null,
            funcionarioNome,
            dataHoraEntrada != null ? dataHoraEntrada.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null,
            dataHoraSaida != null ? dataHoraSaida.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null,
            horasTrabalhadas != null ? horasTrabalhadas.toString() : null,
            projetoId != null ? projetoId.toString() : null,
            projetoNome
        );
    }
}

