package com.zenotime.dto;

import com.zenotime.entity.Solicitacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoCriadaMessage {
    private String solicitacaoId;
    private String funcionarioId;
    private String funcionarioNome;
    private String tipo;
    private String dataInicio;
    private String dataFim;
    private String justificativa;
    private String dataSolicitacao;

    // Método estático para criar mensagem a partir de LocalDate/LocalDateTime
    public static SolicitacaoCriadaMessage create(Long solicitacaoId, Long funcionarioId, String funcionarioNome,
                                                 Solicitacao.TipoSolicitacao tipo, LocalDate dataInicio,
                                                 LocalDate dataFim, String justificativa, LocalDateTime dataSolicitacao) {
        return new SolicitacaoCriadaMessage(
            solicitacaoId != null ? solicitacaoId.toString() : null,
            funcionarioId != null ? funcionarioId.toString() : null,
            funcionarioNome,
            tipo != null ? tipo.toString() : null,
            dataInicio != null ? dataInicio.format(DateTimeFormatter.ISO_LOCAL_DATE) : null,
            dataFim != null ? dataFim.format(DateTimeFormatter.ISO_LOCAL_DATE) : null,
            justificativa,
            dataSolicitacao != null ? dataSolicitacao.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null
        );
    }
}

