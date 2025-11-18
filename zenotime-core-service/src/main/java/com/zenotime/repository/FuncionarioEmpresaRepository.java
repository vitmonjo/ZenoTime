package com.zenotime.repository;

import com.zenotime.entity.FuncionarioEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioEmpresaRepository extends JpaRepository<FuncionarioEmpresa, FuncionarioEmpresa.FuncionarioEmpresaId> {
}

