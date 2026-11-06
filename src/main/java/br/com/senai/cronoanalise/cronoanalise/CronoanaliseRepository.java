package br.com.senai.cronoanalise.cronoanalise;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CronoanaliseRepository extends JpaRepository<Cronoanalise, UUID> {

    @EntityGraph(attributePaths = {"criadoPor", "operacoes"})
    @Query("""
            select distinct c from Cronoanalise c
            where :busca = '' or lower(c.produtoNome) like concat('%', :busca, '%')
               or lower(c.produtoCodigo) like concat('%', :busca, '%')
               or lower(c.empresaNome) like concat('%', :busca, '%')
               or lower(c.cronoanalista) like concat('%', :busca, '%')
            order by c.criadoEm desc""")
    List<Cronoanalise> buscar(String busca);

    long countByCriadoPorId(UUID usuarioId);
}
