package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.Operacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperacaoRepository extends JpaRepository<Operacao, String> {

    @Query("SELECT c FROM Operacao c WHERE c.dataBloqueio = null")
    List<Operacao> findAll();

    /**
     * @param oid
     * @return lista de clientes com primeiro nome igual ao informado
     */
    @Query("SELECT c FROM Operacao c WHERE c.oid = ?1")
    Operacao findByOid(String oid);
}
