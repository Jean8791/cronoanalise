package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, String> {

    @Query("SELECT p FROM Produto p WHERE p.oid = ?1 order by p.dataCriacao asc")
    Produto findByOid(String oid);

    @Query("SELECT p FROM Produto p WHERE p.nome LIKE %?1%")
    Produto findByNome(String nome);

    @Query("SELECT p FROM Produto p WHERE p.dataBloqueio is null order by p.dataCriacao desc")
    List<Produto> findAllNaoBloqueados();


}
