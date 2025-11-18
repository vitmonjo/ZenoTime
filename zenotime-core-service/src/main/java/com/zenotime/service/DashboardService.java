package com.zenotime.service;

import com.zenotime.dto.DashboardDTO;
import com.zenotime.entity.FuncionarioTime;
import com.zenotime.entity.Projeto;
import com.zenotime.entity.RegistroPonto;
import com.zenotime.entity.Time;
import com.zenotime.entity.Usuario;
import com.zenotime.repository.FuncionarioTimeRepository;
import com.zenotime.repository.ProjetoRepository;
import com.zenotime.repository.RegistroPontoRepository;
import com.zenotime.repository.TimeRepository;
import com.zenotime.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    
    @Autowired
    private RegistroPontoRepository registroPontoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ProjetoRepository projetoRepository;
    
    @Autowired
    private TimeRepository timeRepository;
    
    @Autowired
    private FuncionarioTimeRepository funcionarioTimeRepository;
    
    public DashboardDTO obterDadosDashboard() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());
        LocalDateTime inicioMesDateTime = inicioMes.atStartOfDay();
        LocalDateTime fimMesDateTime = fimMes.atTime(23, 59, 59);
        
        // Total de horas do mês
        List<RegistroPonto> registrosMes = registroPontoRepository.findAll().stream()
            .filter(r -> r.getDataHoraEntrada() != null && 
                        r.getDataHoraEntrada().isAfter(inicioMesDateTime.minusDays(1)) &&
                        r.getDataHoraEntrada().isBefore(fimMesDateTime.plusDays(1)) &&
                        r.getHorasTrabalhadas() != null)
            .collect(Collectors.toList());
        
        Double totalHoras = registrosMes.stream()
            .mapToDouble(r -> r.getHorasTrabalhadas() != null ? r.getHorasTrabalhadas() : 0.0)
            .sum();
        
        // Funcionários ativos
        long funcionariosAtivos = usuarioRepository.findAll().stream()
            .filter(u -> u.getTipo() == Usuario.TipoUsuario.FUNCIONARIO && u.getAtivo())
            .count();
        
        // Projetos ativos
        long projetosAtivos = projetoRepository.findAll().stream()
            .filter(p -> p.getAtivo())
            .count();
        
        // Média diária
        long diasUteis = contarDiasUteis(inicioMes, hoje);
        Double mediaDiaria = diasUteis > 0 ? totalHoras / diasUteis : 0.0;
        
        // Produtividade semanal (última semana)
        LocalDate inicioSemana = hoje.minusDays(hoje.getDayOfWeek().getValue() - 1);
        List<DashboardDTO.ProdutividadeSemanal> produtividadeSemanal = new ArrayList<>();
        String[] diasSemana = {"Seg", "Ter", "Qua", "Qui", "Sex"};
        
        for (int i = 0; i < 5; i++) {
            LocalDate dia = inicioSemana.plusDays(i);
            LocalDateTime inicioDia = dia.atStartOfDay();
            LocalDateTime fimDia = dia.atTime(23, 59, 59);
            
            Double horasDia = registrosMes.stream()
                .filter(r -> r.getDataHoraEntrada() != null &&
                            r.getDataHoraEntrada().isAfter(inicioDia.minusSeconds(1)) &&
                            r.getDataHoraEntrada().isBefore(fimDia.plusSeconds(1)) &&
                            r.getHorasTrabalhadas() != null)
                .mapToDouble(r -> r.getHorasTrabalhadas())
                .sum();
            
            produtividadeSemanal.add(new DashboardDTO.ProdutividadeSemanal(
                diasSemana[i], horasDia
            ));
        }
        
        // Distribuição de horas por projeto
        Map<Projeto, Double> horasPorProjeto = new HashMap<>();
        for (RegistroPonto registro : registrosMes) {
            if (registro.getProjeto() != null && registro.getHorasTrabalhadas() != null) {
                horasPorProjeto.merge(registro.getProjeto(), registro.getHorasTrabalhadas(), Double::sum);
            }
        }
        
        List<DashboardDTO.DistribuicaoHoras> distribuicaoHoras = horasPorProjeto.entrySet().stream()
            .sorted(Map.Entry.<Projeto, Double>comparingByValue().reversed())
            .limit(4)
            .map(entry -> {
                Double percentual = totalHoras > 0 ? (entry.getValue() / totalHoras) * 100 : 0.0;
                return new DashboardDTO.DistribuicaoHoras(
                    entry.getKey().getNome(),
                    entry.getValue(),
                    percentual
                );
            })
            .collect(Collectors.toList());
        
        // Comparativo de produtividade entre times
        // Buscar todos os times ativos e calcular horas trabalhadas pelos funcionários de cada time
        List<Time> timesAtivos = timeRepository.findByAtivoTrue();
        Map<Time, Double> horasPorTime = new HashMap<>();
        
        for (Time time : timesAtivos) {
            // Buscar funcionários do time
            List<FuncionarioTime> funcionariosTime = funcionarioTimeRepository.findByTimeId(time.getId());
            Set<Long> funcionarioIds = funcionariosTime.stream()
                .map(ft -> ft.getFuncionario().getId())
                .collect(Collectors.toSet());
            
            // Somar horas trabalhadas pelos funcionários deste time no mês
            Double horasTime = registrosMes.stream()
                .filter(r -> r.getHorasTrabalhadas() != null && 
                            funcionarioIds.contains(r.getFuncionario().getId()))
                .mapToDouble(r -> r.getHorasTrabalhadas())
                .sum();
            
            if (horasTime > 0) {
                horasPorTime.put(time, horasTime);
            }
        }
        
        List<DashboardDTO.ComparativoTimes> comparativoTimes = horasPorTime.entrySet().stream()
            .sorted(Map.Entry.<Time, Double>comparingByValue().reversed())
            .limit(3)
            .map(entry -> new DashboardDTO.ComparativoTimes(
                entry.getKey().getNome(),
                entry.getValue()
            ))
            .collect(Collectors.toList());
        
        return new DashboardDTO(
            totalHoras.longValue(),
            (int) funcionariosAtivos,
            (int) projetosAtivos,
            mediaDiaria,
            produtividadeSemanal,
            distribuicaoHoras,
            comparativoTimes
        );
    }
    
    private long contarDiasUteis(LocalDate inicio, LocalDate fim) {
        long dias = 0;
        LocalDate data = inicio;
        while (!data.isAfter(fim)) {
            if (data.getDayOfWeek() != DayOfWeek.SATURDAY && 
                data.getDayOfWeek() != DayOfWeek.SUNDAY) {
                dias++;
            }
            data = data.plusDays(1);
        }
        return dias;
    }
}

