package com.zenotime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "sprints")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sprint {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private LocalDate dataInicio;
    
    @Column(nullable = false)
    private LocalDate dataFim;
    
    @ManyToOne
    @JoinColumn(name = "time_id", nullable = false)
    private Time time;
    
    @Column(columnDefinition = "TEXT")
    private String objetivo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSprint status = StatusSprint.PLANEJADA;
    
    public enum StatusSprint {
        PLANEJADA,
        EM_ANDAMENTO,
        CONCLUIDA,
        CANCELADA
    }
}

