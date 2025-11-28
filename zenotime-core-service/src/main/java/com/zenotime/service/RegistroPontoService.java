package com.zenotime.service;

import com.zenotime.dto.PontoRegistradoMessage;
import com.zenotime.dto.RegistroHorasRequest;
import com.zenotime.dto.RegistroPontoRequest;
import com.zenotime.dto.RegistroPontoResponse;
import com.zenotime.entity.Projeto;
import com.zenotime.entity.RegistroPonto;
import com.zenotime.entity.Usuario;
import com.zenotime.repository.ProjetoRepository;
import com.zenotime.repository.RegistroPontoRepository;
import com.zenotime.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistroPontoService {
    
    @Autowired
    private RegistroPontoRepository registroPontoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ProjetoRepository projetoRepository;
    
    @Autowired
    private RabbitMQProducerService rabbitMQProducerService;
    
    public RegistroPontoResponse registrarEntrada(Long funcionarioId, RegistroPontoRequest request) {
        Usuario funcionario = usuarioRepository.findById(funcionarioId)
            .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
        
        RegistroPonto registro = new RegistroPonto();
        registro.setFuncionario(funcionario);
        registro.setDataHoraEntrada(request.getDataHoraEntrada());
        registro.setTipo(request.getTipo());
        registro.setObservacoes(request.getObservacoes());
        
        if (request.getProjetoId() != null) {
            Projeto projeto = projetoRepository.findById(request.getProjetoId())
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
            registro.setProjeto(projeto);
        }
        
        registro = registroPontoRepository.save(registro);
        
        // Enviar mensagem para RabbitMQ
        PontoRegistradoMessage message = PontoRegistradoMessage.create(
            registro.getId(),
            registro.getFuncionario().getId(),
            registro.getFuncionario().getNome(),
            registro.getDataHoraEntrada(),
            registro.getDataHoraSaida(),
            registro.getHorasTrabalhadas(),
            registro.getProjeto() != null ? registro.getProjeto().getId() : null,
            registro.getProjeto() != null ? registro.getProjeto().getNome() : null
        );
        rabbitMQProducerService.enviarPontoRegistrado(message);
        
        return toResponse(registro);
    }
    
    public RegistroPontoResponse registrarSaida(Long funcionarioId, Long registroId) {
        RegistroPonto registro = registroPontoRepository.findById(registroId)
            .orElseThrow(() -> new RuntimeException("Registro não encontrado"));
        
        if (!registro.getFuncionario().getId().equals(funcionarioId)) {
            throw new RuntimeException("Registro não pertence ao funcionário");
        }
        
        if (registro.getDataHoraSaida() != null) {
            throw new RuntimeException("Saída já registrada");
        }
        
        registro.setDataHoraSaida(LocalDateTime.now());
        
        if (registro.getDataHoraEntrada() != null) {
            Duration duracao = Duration.between(registro.getDataHoraEntrada(), registro.getDataHoraSaida());
            registro.setHorasTrabalhadas(duracao.toHours() + (duracao.toMinutesPart() / 60.0));
        }
        
        registro = registroPontoRepository.save(registro);
        
        // Enviar mensagem para RabbitMQ quando saída for registrada
        PontoRegistradoMessage message = PontoRegistradoMessage.create(
            registro.getId(),
            registro.getFuncionario().getId(),
            registro.getFuncionario().getNome(),
            registro.getDataHoraEntrada(),
            registro.getDataHoraSaida(),
            registro.getHorasTrabalhadas(),
            registro.getProjeto() != null ? registro.getProjeto().getId() : null,
            registro.getProjeto() != null ? registro.getProjeto().getNome() : null
        );
        rabbitMQProducerService.enviarPontoRegistrado(message);
        
        return toResponse(registro);
    }
    
    public List<RegistroPontoResponse> listarRegistros(Long funcionarioId) {
        List<RegistroPonto> registros = registroPontoRepository.findByFuncionarioIdOrderByDataHoraEntradaDesc(funcionarioId);
        return registros.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public RegistroPontoResponse registrarHoras(Long funcionarioId, RegistroHorasRequest request) {
        Usuario funcionario = usuarioRepository.findById(funcionarioId)
            .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        Projeto projeto = projetoRepository.findById(request.getProjetoId())
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        RegistroPonto registro = new RegistroPonto();
        registro.setFuncionario(funcionario);
        registro.setProjeto(projeto);
        registro.setDataHoraEntrada(request.getDataTrabalho().atStartOfDay());
        registro.setHorasTrabalhadas(request.getHorasTrabalhadas());
        registro.setObservacoes(request.getObservacoes());
        registro.setTipo(RegistroPonto.TipoRegistro.valueOf(request.getTipo()));

        registro = registroPontoRepository.save(registro);

        // Enviar mensagem para RabbitMQ
        PontoRegistradoMessage message = PontoRegistradoMessage.create(
            registro.getId(),
            registro.getFuncionario().getId(),
            registro.getFuncionario().getNome(),
            registro.getDataHoraEntrada(),
            null, // dataHoraSaida (não aplicável para registro direto de horas)
            registro.getHorasTrabalhadas(),
            registro.getProjeto() != null ? registro.getProjeto().getId() : null,
            registro.getProjeto() != null ? registro.getProjeto().getNome() : null
        );
        rabbitMQProducerService.enviarPontoRegistrado(message);

        return toResponse(registro);
    }
    
    private RegistroPontoResponse toResponse(RegistroPonto registro) {
        return RegistroPontoResponse.create(registro);
    }
}

