package com.zenotime.repository;

import com.zenotime.entity.RegistroPonto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {
    List<RegistroPonto> findByFuncionarioId(Long funcionarioId);
    List<RegistroPonto> findByFuncionarioIdAndDataHoraEntradaBetween(
        Long funcionarioId, LocalDateTime inicio, LocalDateTime fim);
    List<RegistroPonto> findByProjetoId(Long projetoId);
    
    @Query("SELECT r FROM RegistroPonto r WHERE r.funcionario.id = :funcionarioId " +
           "AND r.dataHoraEntrada >= :inicio AND r.dataHoraEntrada < :fim")
    List<RegistroPonto> findRegistrosPorPeriodo(
        @Param("funcionarioId") Long funcionarioId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );
}

