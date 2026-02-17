package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.Parada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParadaRepository extends JpaRepository<Parada, String> {

    List<Parada> findAll();

    @Query("SELECT p FROM Parada p WHERE p.oid = ?1")
    Parada findByOid(String oid);
}
