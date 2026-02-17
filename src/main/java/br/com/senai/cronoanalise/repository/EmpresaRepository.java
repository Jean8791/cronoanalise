package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, String> {

    @Query("SELECT e FROM Empresa e WHERE e.dataBloqueio is null order by e.dataCriacao desc")
    List<Empresa> findAll();


    Empresa findByOid(String oid);


    @Query("SELECT e FROM Empresa e WHERE e.dataBloqueio is null")
    Empresa findByNome(String nome);
}
