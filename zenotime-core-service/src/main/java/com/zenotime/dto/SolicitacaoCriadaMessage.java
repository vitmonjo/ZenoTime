package com.zenotime.dto;

import com.zenotime.entity.Solicitacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoCriadaMessage {
    private Long solicitacaoId;
    private Long funcionarioId;
    private String funcionarioNome;
    private Solicitacao.TipoSolicitacao tipo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String justificativa;
    private LocalDateTime dataSolicitacao;
}

