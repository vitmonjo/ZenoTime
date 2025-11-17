package com.zenotime.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {
    
    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<String> exportarRelatorio(
            @RequestParam String formato,
            @RequestParam(required = false) String periodo) {
        
        // Em produção, redirecionar para o report-service
        // Por enquanto, retornar mensagem informativa
        return ResponseEntity.ok()
            .body("Relatório será gerado pelo report-service. Formato: " + formato);
    }
}
