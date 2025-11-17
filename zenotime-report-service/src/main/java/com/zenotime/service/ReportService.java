package com.zenotime.service;

import com.zenotime.dto.PontoRegistradoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
    
    @RabbitListener(queues = "ponto.registrado")
    public void processarPontoRegistrado(PontoRegistradoMessage message) {
        logger.info("Recebido registro de ponto: Funcionário {} - {} horas trabalhadas", 
            message.getFuncionarioNome(), 
            message.getHorasTrabalhadas());
        
        // Aqui seria processado para gerar o CSV diário
        // Por enquanto apenas log
    }
}

