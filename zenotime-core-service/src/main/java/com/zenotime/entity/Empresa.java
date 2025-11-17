package com.zenotime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "empresas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(unique = true)
    private String cnpj;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @ManyToMany
    @JoinTable(
        name = "funcionario_empresa",
        joinColumns = @JoinColumn(name = "empresa_id"),
        inverseJoinColumns = @JoinColumn(name = "funcionario_id")
    )
    private Set<Usuario> funcionarios = new HashSet<>();
    
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private Set<Projeto> projetos = new HashSet<>();
    
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private Set<FuncionarioEmpresa> funcionarioEmpresas = new HashSet<>();
}

