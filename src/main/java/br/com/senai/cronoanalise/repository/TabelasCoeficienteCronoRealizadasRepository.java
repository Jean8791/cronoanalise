package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.TabelaCoeficienteCronoRealizadas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TabelasCoeficienteCronoRealizadasRepository extends JpaRepository<TabelaCoeficienteCronoRealizadas, String> {

    TabelaCoeficienteCronoRealizadas findByOid(String oid);
}
