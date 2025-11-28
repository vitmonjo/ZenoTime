package com.zenotime.dto;

import com.zenotime.entity.RegistroPonto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class RegistroPontoResponse {
    private Long id;
    private Long funcionarioId;
    private String funcionarioNome;
    private String dataTrabalho;
    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraSaida;
    private Double horasTrabalhadas;
    private Long projetoId;
    private String projetoNome;
    private String observacoes;
    private RegistroPonto.TipoRegistro tipo;

    // Método auxiliar para criar resposta com data formatada
    public static RegistroPontoResponse create(RegistroPonto registro) {
        String dataTrabalho = registro.getDataHoraEntrada() != null
            ? registro.getDataHoraEntrada().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
            : null;


        return new RegistroPontoResponse(
            registro.getId(),
            registro.getFuncionario().getId(),
            registro.getFuncionario().getNome(),
            dataTrabalho,
            registro.getDataHoraEntrada(),
            registro.getDataHoraSaida(),
            registro.getHorasTrabalhadas(),
            registro.getProjeto() != null ? registro.getProjeto().getId() : null,
            registro.getProjeto() != null ? registro.getProjeto().getNome() : null,
            registro.getObservacoes(),
            registro.getTipo()
        );
    }
}

