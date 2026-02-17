package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.CronoanaliseProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CronoanaliseProdutoRepository extends JpaRepository<CronoanaliseProduto, String> {
}
