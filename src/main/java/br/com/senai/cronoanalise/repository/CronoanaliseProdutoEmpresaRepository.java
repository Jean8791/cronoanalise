package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.CronoanaliseProdutoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CronoanaliseProdutoEmpresaRepository extends JpaRepository<CronoanaliseProdutoEmpresa, String> {

    CronoanaliseProdutoEmpresa findByOid(String oid);
}
