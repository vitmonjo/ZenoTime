package com.zenotime.controller;

import com.zenotime.dto.SprintCriadaMessage;
import com.zenotime.entity.Sprint;
import com.zenotime.repository.SprintRepository;
import com.zenotime.repository.TimeRepository;
import com.zenotime.service.RabbitMQProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sprints")
@CrossOrigin(origins = "*")
public class SprintController {
    
    @Autowired
    private SprintRepository sprintRepository;
    
    @Autowired
    private TimeRepository timeRepository;
    
    @Autowired
    private RabbitMQProducerService rabbitMQProducerService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Sprint>> findAll() {
        return ResponseEntity.ok(sprintRepository.findAll());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Sprint> findById(@PathVariable Long id) {
        return sprintRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/produtividade")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Sprint> findProdutividade(@PathVariable Long id) {
        return sprintRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Sprint> create(@RequestBody Sprint sprint) {
        if (sprint.getTime() != null && sprint.getTime().getId() != null) {
            timeRepository.findById(sprint.getTime().getId())
                .ifPresent(sprint::setTime);
        }
        sprint = sprintRepository.save(sprint);
        
        // Enviar mensagem para RabbitMQ
        SprintCriadaMessage message = new SprintCriadaMessage(
            sprint.getId(),
            sprint.getNome(),
            sprint.getTime().getId(),
            sprint.getTime().getNome(),
            sprint.getDataInicio(),
            sprint.getDataFim(),
            sprint.getObjetivo()
        );
        rabbitMQProducerService.enviarSprintCriada(message);
        
        return ResponseEntity.ok(sprint);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Sprint> update(@PathVariable Long id, @RequestBody Sprint sprint) {
        Optional<Sprint> sprintExistente = sprintRepository.findById(id);
        if (sprintExistente.isPresent()) {
            sprint.setId(id);
            if (sprint.getTime() != null && sprint.getTime().getId() != null) {
                timeRepository.findById(sprint.getTime().getId())
                    .ifPresent(sprint::setTime);
            }
            return ResponseEntity.ok(sprintRepository.save(sprint));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sprintRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

