package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.CronoanaliseProdutoEmpresaParada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CronoanaliseProdutoEmpresaParadaRepository extends JpaRepository<CronoanaliseProdutoEmpresaParada, String> {
}
