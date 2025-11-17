package com.zenotime.dto;

import com.zenotime.entity.Solicitacao;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SolicitacaoRequest {
    @NotNull(message = "Tipo é obrigatório")
    private Solicitacao.TipoSolicitacao tipo;
    
    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;
    
    @NotNull(message = "Data de fim é obrigatória")
    private LocalDate dataFim;
    
    private String justificativa;
}

