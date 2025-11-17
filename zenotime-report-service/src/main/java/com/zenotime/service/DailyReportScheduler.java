package com.zenotime.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class DailyReportScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(DailyReportScheduler.class);
    
    @Scheduled(cron = "0 0 1 * * ?") // Executa diariamente à 1h da manhã
    public void gerarRelatorioDiario() {
        logger.info("Iniciando geração de relatório diário CSV");
        
        try {
            String fileName = "relatorio_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".csv";
            String filePath = "/app/reports/" + fileName;
            
            // Criar diretório se não existir
            java.io.File dir = new java.io.File("/app/reports");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // Criar arquivo CSV com cabeçalho
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.append("Data,Funcionario,Horas Trabalhadas,Projeto\n");
                // Aqui seria adicionado os dados dos registros do dia anterior
                logger.info("Relatório CSV gerado: {}", filePath);
            }
        } catch (IOException e) {
            logger.error("Erro ao gerar relatório CSV", e);
        }
    }
}

