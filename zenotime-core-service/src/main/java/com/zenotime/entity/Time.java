package com.zenotime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "times")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Time {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @ManyToMany
    @JoinTable(
        name = "funcionario_time",
        joinColumns = @JoinColumn(name = "time_id"),
        inverseJoinColumns = @JoinColumn(name = "funcionario_id")
    )
    private Set<Usuario> funcionarios = new HashSet<>();
    
    @OneToMany(mappedBy = "time", cascade = CascadeType.ALL)
    private Set<Sprint> sprints = new HashSet<>();
    
    @OneToMany(mappedBy = "time", cascade = CascadeType.ALL)
    private Set<FuncionarioTime> funcionarioTimes = new HashSet<>();
}

