package com.zenotime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projetos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Projeto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @ManyToMany
    @JoinTable(
        name = "funcionario_projeto",
        joinColumns = @JoinColumn(name = "projeto_id"),
        inverseJoinColumns = @JoinColumn(name = "funcionario_id")
    )
    private Set<Usuario> funcionarios = new HashSet<>();
    
    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL)
    private Set<Time> times = new HashSet<>();
    
    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL)
    private Set<RegistroPonto> registrosPonto = new HashSet<>();
    
    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL)
    private Set<FuncionarioProjeto> funcionarioProjetos = new HashSet<>();
}

