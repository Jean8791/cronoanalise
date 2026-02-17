package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.TabelaCoeficienteDistribuicaoNormal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TabelasCoeficienteDistribuicaoNormalRepository extends JpaRepository<TabelaCoeficienteDistribuicaoNormal, String> {

    TabelaCoeficienteDistribuicaoNormal findByOid(String oid);
}
