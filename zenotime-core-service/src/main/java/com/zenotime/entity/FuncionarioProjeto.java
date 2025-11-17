package com.zenotime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "funcionario_projeto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioProjeto {
    
    @EmbeddedId
    private FuncionarioProjetoId id;
    
    @ManyToOne
    @MapsId("funcionarioId")
    @JoinColumn(name = "funcionario_id")
    private Usuario funcionario;
    
    @ManyToOne
    @MapsId("projetoId")
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;
    
    @Column(nullable = false)
    private LocalDate dataInicio;
    
    private LocalDate dataFim;
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FuncionarioProjetoId implements java.io.Serializable {
        private Long funcionarioId;
        private Long projetoId;
    }
}

