package com.zenotime.repository;

import com.zenotime.entity.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    List<Solicitacao> findByFuncionarioId(Long funcionarioId);
    List<Solicitacao> findByStatus(Solicitacao.StatusSolicitacao status);
}

