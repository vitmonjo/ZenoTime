package com.zenotime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "funcionario_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioTime {
    
    @EmbeddedId
    private FuncionarioTimeId id;
    
    @ManyToOne
    @MapsId("funcionarioId")
    @JoinColumn(name = "funcionario_id")
    private Usuario funcionario;
    
    @ManyToOne
    @MapsId("timeId")
    @JoinColumn(name = "time_id")
    private Time time;
    
    @Column(nullable = false)
    private LocalDate dataInicio;
    
    private LocalDate dataFim;
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FuncionarioTimeId implements java.io.Serializable {
        private Long funcionarioId;
        private Long timeId;
    }
}

