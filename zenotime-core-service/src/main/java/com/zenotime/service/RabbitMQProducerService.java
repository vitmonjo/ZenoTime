package com.zenotime.service;

import com.zenotime.config.RabbitMQConfig;
import com.zenotime.dto.PontoRegistradoMessage;
import com.zenotime.dto.SolicitacaoCriadaMessage;
import com.zenotime.dto.SprintCriadaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducerService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQProducerService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void enviarPontoRegistrado(PontoRegistradoMessage message) {
        logger.info("Enviando mensagem para ponto.registrado: {}", message);
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            "ponto.registrado",
            message
        );
        logger.info("Mensagem enviada para ponto.registrado");
    }

    public void enviarSolicitacaoCriada(SolicitacaoCriadaMessage message) {
        logger.info("Enviando mensagem para solicitacao.criada: {}", message);
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            "solicitacao.criada",
            message
        );
        logger.info("Mensagem enviada para solicitacao.criada");
    }

    public void enviarSprintCriada(SprintCriadaMessage message) {
        logger.info("Enviando mensagem para sprint.criada: {}", message);
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            "sprint.criada",
            message
        );
        logger.info("Mensagem enviada para sprint.criada");
    }
}

