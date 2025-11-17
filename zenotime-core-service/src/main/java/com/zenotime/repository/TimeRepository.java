package com.zenotime.repository;

import com.zenotime.entity.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {
    List<Time> findByProjetoId(Long projetoId);
    List<Time> findByAtivoTrue();
}

