package com.zenotime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "registros_ponto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroPonto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Usuario funcionario;
    
    @Column(nullable = false)
    private LocalDateTime dataHoraEntrada;
    
    private LocalDateTime dataHoraSaida;
    
    private Double horasTrabalhadas;
    
    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;
    
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRegistro tipo = TipoRegistro.NORMAL;
    
    public enum TipoRegistro {
        NORMAL,
        ADMINISTRATIVO
    }
}

