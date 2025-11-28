package com.zenotime.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroHorasRequest {
    @NotNull(message = "Projeto é obrigatório")
    private Long projetoId;

    @NotNull(message = "Data do trabalho é obrigatória")
    private LocalDate dataTrabalho;

    @NotNull(message = "Horas trabalhadas é obrigatório")
    private Double horasTrabalhadas;

    private String observacoes;
    private String tipo = "NORMAL";
}
