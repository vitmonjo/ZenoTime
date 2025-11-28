package com.zenotime.service;

import com.zenotime.dto.SolicitacaoCriadaMessage;
import com.zenotime.dto.SprintCriadaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService() {
        logger.info("NotificationService inicializado - listeners configurados");
    }
    
    @RabbitListener(queues = "solicitacao.criada")
    public void processarSolicitacaoCriada(SolicitacaoCriadaMessage message) {
        logger.warn("ALERTA ADMINISTRADOR: Nova solicitação de {} criada por {} - Período: {} a {}", 
            message.getTipo(),
            message.getFuncionarioNome(),
            message.getDataInicio(),
            message.getDataFim());
        
        // Aqui seria enviado notificação real para administradores
        // Por enquanto apenas log
    }
    
    @RabbitListener(queues = "sprint.criada")
    public void processarSprintCriada(SprintCriadaMessage message) {
        logger.info("Nova sprint criada: {} - Time: {} - Período: {} a {}", 
            message.getSprintNome(),
            message.getTimeNome(),
            message.getDataInicio(),
            message.getDataFim());
        
        // Aqui seria enviado notificação para os times
        // Por enquanto apenas log
    }
}

