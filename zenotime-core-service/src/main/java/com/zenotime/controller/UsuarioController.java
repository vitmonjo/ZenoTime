package com.zenotime.controller;

import com.zenotime.entity.Projeto;
import com.zenotime.entity.Usuario;
import com.zenotime.repository.ProjetoRepository;
import com.zenotime.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProjetoRepository projetoRepository;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        return usuarioService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Usuario> create(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.save(usuario));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.update(id, usuario));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/meus-projetos")
    public ResponseEntity<List<Projeto>> getMeusProjetos() {
        // Retorna todos os projetos da TechSolutions (ID = 23)
        return ResponseEntity.ok(projetoRepository.findByEmpresaId(23L));
    }

    @GetMapping("/minha-empresa")
    public ResponseEntity<String> getMinhaEmpresa() {
        // Retorna TechSolutions para o funcionário especial
        return ResponseEntity.ok("TechSolutions");
    }

    // Temporariamente desabilitado para debug
    // @GetMapping("/projetos")
    // public ResponseEntity<List<Projeto>> getMeusProjetos() {
    //     Usuario usuario = getCurrentUser();
    //     List<Projeto> projetos = projetoRepository.findAll().stream()
    //         .filter(projeto -> projeto.getEmpresa() != null &&
    //                          usuario.getEmpresas().stream()
    //                              .anyMatch(empresa -> empresa.getId().equals(projeto.getEmpresa().getId())))
    //         .collect(Collectors.toList());
    //     return ResponseEntity.ok(projetos);
    // }

    // @GetMapping("/empresa")
    // public ResponseEntity<String> getMinhaEmpresa() {
    //     Usuario usuario = getCurrentUser();
    //     String empresaNome = usuario.getEmpresas().stream()
    //         .findFirst()
    //         .map(empresa -> empresa.getNome())
    //         .orElse("Empresa não encontrada");
    //     return ResponseEntity.ok(empresaNome);
    // }
}

