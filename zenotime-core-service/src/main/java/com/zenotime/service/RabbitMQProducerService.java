package com.zenotime.service;

import com.zenotime.config.RabbitMQConfig;
import com.zenotime.dto.PontoRegistradoMessage;
import com.zenotime.dto.SolicitacaoCriadaMessage;
import com.zenotime.dto.SprintCriadaMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducerService {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void enviarPontoRegistrado(PontoRegistradoMessage message) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            "ponto.registrado",
            message
        );
    }
    
    public void enviarSolicitacaoCriada(SolicitacaoCriadaMessage message) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            "solicitacao.criada",
            message
        );
    }
    
    public void enviarSprintCriada(SprintCriadaMessage message) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            "sprint.criada",
            message
        );
    }
}

