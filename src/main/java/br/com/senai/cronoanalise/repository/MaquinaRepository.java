package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MaquinaRepository extends JpaRepository<Maquina, String> {

    Maquina findByOid(String oid);

    @Query("SELECT m FROM Maquina m WHERE m.nome LIKE %?1%")
    Maquina findByNome(String nome);
}
