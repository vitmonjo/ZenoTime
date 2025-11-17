package com.zenotime.controller;

import com.zenotime.dto.SolicitacaoCriadaMessage;
import com.zenotime.dto.SolicitacaoRequest;
import com.zenotime.entity.Solicitacao;
import com.zenotime.repository.SolicitacaoRepository;
import com.zenotime.repository.UsuarioRepository;
import com.zenotime.service.RabbitMQProducerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/solicitacoes")
@CrossOrigin(origins = "*")
public class SolicitacaoController {
    
    @Autowired
    private SolicitacaoRepository solicitacaoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RabbitMQProducerService rabbitMQProducerService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'FUNCIONARIO')")
    public ResponseEntity<List<Solicitacao>> findAll() {
        return ResponseEntity.ok(solicitacaoRepository.findAll());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'FUNCIONARIO')")
    public ResponseEntity<Solicitacao> findById(@PathVariable Long id) {
        return solicitacaoRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('FUNCIONARIO')")
    public ResponseEntity<Solicitacao> create(@Valid @RequestBody SolicitacaoRequest request) {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setTipo(request.getTipo());
        solicitacao.setDataInicio(request.getDataInicio());
        solicitacao.setDataFim(request.getDataFim());
        solicitacao.setJustificativa(request.getJustificativa());
        solicitacao.setDataSolicitacao(LocalDateTime.now());
        solicitacao.setStatus(Solicitacao.StatusSolicitacao.PENDENTE);
        
        // Em produção, buscar usuário logado
        usuarioRepository.findById(1L).ifPresent(solicitacao::setFuncionario);
        
        solicitacao = solicitacaoRepository.save(solicitacao);
        
        // Enviar mensagem para RabbitMQ
        SolicitacaoCriadaMessage message = new SolicitacaoCriadaMessage(
            solicitacao.getId(),
            solicitacao.getFuncionario().getId(),
            solicitacao.getFuncionario().getNome(),
            solicitacao.getTipo(),
            solicitacao.getDataInicio(),
            solicitacao.getDataFim(),
            solicitacao.getJustificativa(),
            solicitacao.getDataSolicitacao()
        );
        rabbitMQProducerService.enviarSolicitacaoCriada(message);
        
        return ResponseEntity.ok(solicitacao);
    }
    
    @PutMapping("/{id}/aprovar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Solicitacao> aprovar(@PathVariable Long id) {
        Optional<Solicitacao> solicitacao = solicitacaoRepository.findById(id);
        if (solicitacao.isPresent()) {
            solicitacao.get().setStatus(Solicitacao.StatusSolicitacao.APROVADA);
            return ResponseEntity.ok(solicitacaoRepository.save(solicitacao.get()));
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/rejeitar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Solicitacao> rejeitar(@PathVariable Long id) {
        Optional<Solicitacao> solicitacao = solicitacaoRepository.findById(id);
        if (solicitacao.isPresent()) {
            solicitacao.get().setStatus(Solicitacao.StatusSolicitacao.REJEITADA);
            return ResponseEntity.ok(solicitacaoRepository.save(solicitacao.get()));
        }
        return ResponseEntity.notFound().build();
    }
}

