package com.zenotime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "funcionario_empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioEmpresa {
    
    @EmbeddedId
    private FuncionarioEmpresaId id;
    
    @ManyToOne
    @MapsId("funcionarioId")
    @JoinColumn(name = "funcionario_id")
    private Usuario funcionario;
    
    @ManyToOne
    @MapsId("empresaId")
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
    
    @Column(nullable = false)
    private LocalDate dataInicio;
    
    private LocalDate dataFim;
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FuncionarioEmpresaId implements java.io.Serializable {
        private Long funcionarioId;
        private Long empresaId;
    }
}

