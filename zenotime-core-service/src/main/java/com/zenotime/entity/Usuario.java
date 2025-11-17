package com.zenotime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String senha;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @ManyToMany(mappedBy = "funcionarios")
    private Set<Empresa> empresas = new HashSet<>();
    
    @ManyToMany(mappedBy = "funcionarios")
    private Set<Projeto> projetos = new HashSet<>();
    
    @ManyToMany(mappedBy = "funcionarios")
    private Set<Time> times = new HashSet<>();
    
    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
    private Set<RegistroPonto> registrosPonto = new HashSet<>();
    
    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
    private Set<Solicitacao> solicitacoes = new HashSet<>();
    
    public enum TipoUsuario {
        FUNCIONARIO,
        ADMINISTRADOR
    }
}

