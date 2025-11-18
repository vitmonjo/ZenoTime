package com.zenotime.repository;

import com.zenotime.entity.FuncionarioTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionarioTimeRepository extends JpaRepository<FuncionarioTime, FuncionarioTime.FuncionarioTimeId> {
    List<FuncionarioTime> findByTimeId(Long timeId);
    List<FuncionarioTime> findByFuncionarioId(Long funcionarioId);
}

