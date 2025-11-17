package com.zenotime.controller;

import com.zenotime.entity.Time;
import com.zenotime.repository.ProjetoRepository;
import com.zenotime.repository.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/times")
@CrossOrigin(origins = "*")
public class TimeController {
    
    @Autowired
    private TimeRepository timeRepository;
    
    @Autowired
    private ProjetoRepository projetoRepository;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Time>> findAll() {
        return ResponseEntity.ok(timeRepository.findAll());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Time> findById(@PathVariable Long id) {
        return timeRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Time> create(@RequestBody Time time) {
        if (time.getProjeto() != null && time.getProjeto().getId() != null) {
            projetoRepository.findById(time.getProjeto().getId())
                .ifPresent(time::setProjeto);
        }
        return ResponseEntity.ok(timeRepository.save(time));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Time> update(@PathVariable Long id, @RequestBody Time time) {
        Optional<Time> timeExistente = timeRepository.findById(id);
        if (timeExistente.isPresent()) {
            time.setId(id);
            if (time.getProjeto() != null && time.getProjeto().getId() != null) {
                projetoRepository.findById(time.getProjeto().getId())
                    .ifPresent(time::setProjeto);
            }
            return ResponseEntity.ok(timeRepository.save(time));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

