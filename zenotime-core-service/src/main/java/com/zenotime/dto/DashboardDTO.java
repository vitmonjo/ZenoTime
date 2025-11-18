package com.zenotime.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private Long totalHoras;
    private Integer funcionariosAtivos;
    private Integer projetosAtivos;
    private Double mediaDiaria;
    private List<ProdutividadeSemanal> produtividadeSemanal;
    private List<DistribuicaoHoras> distribuicaoHoras;
    private List<ComparativoTimes> comparativoTimes;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProdutividadeSemanal {
        private String dia;
        private Double horas;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DistribuicaoHoras {
        private String projeto;
        private Double horas;
        private Double percentual;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComparativoTimes {
        private String time;
        private Double horas;
    }
}

