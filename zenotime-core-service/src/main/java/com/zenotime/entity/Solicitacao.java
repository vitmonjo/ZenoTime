package com.zenotime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Solicitacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Usuario funcionario;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSolicitacao tipo;
    
    @Column(nullable = false)
    private LocalDate dataInicio;
    
    @Column(nullable = false)
    private LocalDate dataFim;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao status = StatusSolicitacao.PENDENTE;
    
    @Column(columnDefinition = "TEXT")
    private String justificativa;
    
    @Column(nullable = false)
    private LocalDateTime dataSolicitacao = LocalDateTime.now();
    
    public enum TipoSolicitacao {
        FERIAS,
        ATESTADO
    }
    
    public enum StatusSolicitacao {
        PENDENTE,
        APROVADA,
        REJEITADA
    }
}

