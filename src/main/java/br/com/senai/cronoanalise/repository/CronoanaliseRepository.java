package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.Cronoanalise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CronoanaliseRepository extends JpaRepository<Cronoanalise, String> {

    @Query("SELECT c FROM Cronoanalise c WHERE c.dataBloqueio is null order by c.dataCriacao desc")
    List<Cronoanalise> findAll();
    
    @Query(value = "select * from cronoanalise order by data_criacao desc limit 1", nativeQuery = true)
    Cronoanalise findUltimoAdicionado();

    Cronoanalise findByOid(String oid);
}