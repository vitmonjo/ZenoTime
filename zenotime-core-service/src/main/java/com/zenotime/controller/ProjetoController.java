package com.zenotime.controller;

import com.zenotime.entity.Projeto;
import com.zenotime.repository.EmpresaRepository;
import com.zenotime.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projetos")
@CrossOrigin(origins = "*")
public class ProjetoController {
    
    @Autowired
    private ProjetoRepository projetoRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Projeto>> findAll() {
        return ResponseEntity.ok(projetoRepository.findAll());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Projeto> findById(@PathVariable Long id) {
        return projetoRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/funcionarios")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Projeto> findFuncionarios(@PathVariable Long id) {
        return projetoRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Projeto> create(@RequestBody Projeto projeto) {
        if (projeto.getEmpresa() != null && projeto.getEmpresa().getId() != null) {
            empresaRepository.findById(projeto.getEmpresa().getId())
                .ifPresent(projeto::setEmpresa);
        }
        return ResponseEntity.ok(projetoRepository.save(projeto));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Projeto> update(@PathVariable Long id, @RequestBody Projeto projeto) {
        Optional<Projeto> projetoExistente = projetoRepository.findById(id);
        if (projetoExistente.isPresent()) {
            projeto.setId(id);
            if (projeto.getEmpresa() != null && projeto.getEmpresa().getId() != null) {
                empresaRepository.findById(projeto.getEmpresa().getId())
                    .ifPresent(projeto::setEmpresa);
            }
            return ResponseEntity.ok(projetoRepository.save(projeto));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projetoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

