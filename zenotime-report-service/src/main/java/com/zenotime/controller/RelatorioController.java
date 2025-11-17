package com.zenotime.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {
    
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportarRelatorio(
            @RequestParam String formato,
            @RequestParam(required = false) String periodo) {
        
        try {
            byte[] arquivo;
            String nomeArquivo;
            MediaType mediaType;
            
            switch (formato.toUpperCase()) {
                case "CSV":
                    arquivo = gerarCSV(periodo);
                    nomeArquivo = "relatorio_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".csv";
                    mediaType = MediaType.parseMediaType("text/csv");
                    break;
                case "PDF":
                    arquivo = gerarPDF(periodo);
                    nomeArquivo = "relatorio_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".pdf";
                    mediaType = MediaType.APPLICATION_PDF;
                    break;
                case "EXCEL":
                case "XLSX":
                    arquivo = gerarExcel(periodo);
                    nomeArquivo = "relatorio_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".xlsx";
                    mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    break;
                default:
                    return ResponseEntity.badRequest().build();
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setContentDispositionFormData("attachment", nomeArquivo);
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(arquivo);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    private byte[] gerarCSV(String periodo) throws IOException {
        StringBuilder csv = new StringBuilder();
        csv.append("Data,Funcionario,Horas Trabalhadas,Projeto\n");
        csv.append("2024-01-01,João Silva,8.0,Projeto A\n");
        csv.append("2024-01-01,Maria Santos,7.5,Projeto B\n");
        
        return csv.toString().getBytes();
    }
    
    private byte[] gerarPDF(String periodo) throws IOException {
        // Implementação básica - em produção usar iText ou similar
        String pdfContent = "%PDF-1.4\nRelatório de Horas\n";
        return pdfContent.getBytes();
    }
    
    private byte[] gerarExcel(String periodo) throws IOException {
        // Implementação básica - em produção usar Apache POI
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Relatório de Horas".getBytes());
        return outputStream.toByteArray();
    }
}

