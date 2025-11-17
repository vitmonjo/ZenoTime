package com.zenotime.repository;

import com.zenotime.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
    List<Projeto> findByEmpresaId(Long empresaId);
    List<Projeto> findByAtivoTrue();
}

