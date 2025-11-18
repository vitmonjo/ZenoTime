package com.zenotime.controller;

import com.zenotime.dto.DashboardDTO;
import com.zenotime.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<DashboardDTO> obterDadosDashboard() {
        return ResponseEntity.ok(dashboardService.obterDadosDashboard());
    }
}

