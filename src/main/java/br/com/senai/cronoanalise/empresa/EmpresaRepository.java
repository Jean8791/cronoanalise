package br.com.senai.cronoanalise.empresa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {

    boolean existsByCnpj(String cnpj);

    boolean existsByCnpjAndIdNot(String cnpj, UUID id);

    @Query("""
            select e from Empresa e
            where (:incluirBloqueadas = true or e.bloqueadoEm is null)
              and (:busca = '' or lower(e.nome) like concat('%', :busca, '%') or e.cnpj like concat('%', :busca, '%'))
            order by e.nome""")
    List<Empresa> buscar(String busca, boolean incluirBloqueadas);
}
