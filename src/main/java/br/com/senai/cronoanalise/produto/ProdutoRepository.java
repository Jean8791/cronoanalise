package br.com.senai.cronoanalise.produto;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

    boolean existsByEmpresaIdAndCodigoIgnoreCase(UUID empresaId, String codigo);

    boolean existsByEmpresaIdAndCodigoIgnoreCaseAndIdNot(UUID empresaId, String codigo, UUID id);

    @EntityGraph(attributePaths = "empresa")
    @Query("""
            select p from Produto p
            where (:incluirBloqueados = true or p.bloqueadoEm is null)
              and (:empresaId is null or p.empresa.id = :empresaId)
              and (:busca = '' or lower(p.nome) like concat('%', :busca, '%') or lower(p.codigo) like concat('%', :busca, '%'))
            order by p.nome""")
    List<Produto> buscar(String busca, UUID empresaId, boolean incluirBloqueados);
}
