package com.zenotime.controller;

import com.zenotime.dto.RegistroHorasRequest;
import com.zenotime.dto.RegistroPontoRequest;
import com.zenotime.dto.RegistroPontoResponse;
import com.zenotime.entity.Usuario;
import com.zenotime.repository.UsuarioRepository;
import com.zenotime.service.RegistroPontoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ponto")
@CrossOrigin(origins = "*")
public class RegistroPontoController {
    
    @Autowired
    private RegistroPontoService registroPontoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        // Busca o usuário logado
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return usuario.getId();
    }
    
    @GetMapping
    public ResponseEntity<List<RegistroPontoResponse>> listarRegistros() {
        Long funcionarioId = getCurrentUserId();
        return ResponseEntity.ok(registroPontoService.listarRegistros(funcionarioId));
    }
    
    @PostMapping("/entrada")
    public ResponseEntity<RegistroPontoResponse> registrarEntrada(@Valid @RequestBody RegistroPontoRequest request) {
        Long funcionarioId = getCurrentUserId();
        return ResponseEntity.ok(registroPontoService.registrarEntrada(funcionarioId, request));
    }
    
    @PostMapping("/saida/{registroId}")
    public ResponseEntity<RegistroPontoResponse> registrarSaida(@PathVariable Long registroId) {
        Long funcionarioId = getCurrentUserId();
        return ResponseEntity.ok(registroPontoService.registrarSaida(funcionarioId, registroId));
    }

    @PostMapping("/registrar-horas")
    public ResponseEntity<RegistroPontoResponse> registrarHoras(@Valid @RequestBody RegistroHorasRequest request) {
        Long funcionarioId = getCurrentUserId();
        return ResponseEntity.ok(registroPontoService.registrarHoras(funcionarioId, request));
    }
}

