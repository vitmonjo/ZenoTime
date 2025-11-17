package com.zenotime.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprintCriadaMessage {
    private Long sprintId;
    private String sprintNome;
    private Long timeId;
    private String timeNome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String objetivo;
}

