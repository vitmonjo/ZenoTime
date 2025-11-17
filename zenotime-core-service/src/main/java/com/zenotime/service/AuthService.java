package com.zenotime.service;

import com.zenotime.config.JwtUtil;
import com.zenotime.dto.LoginRequest;
import com.zenotime.dto.LoginResponse;
import com.zenotime.entity.Usuario;
import com.zenotime.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));
        
        if (!usuario.getAtivo()) {
            throw new RuntimeException("Usuário inativo");
        }
        
        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }
        
        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getTipo().name());
        
        return new LoginResponse(
            token,
            usuario.getTipo().name(),
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail()
        );
    }
}

