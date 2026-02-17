package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.CronoanaliseProdutoOperacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CronoanaliseProdutoOperacaoRepository extends JpaRepository<CronoanaliseProdutoOperacao, String> {

    CronoanaliseProdutoOperacao findByOid(String oid);
}
