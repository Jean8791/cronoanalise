package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {


    @Query("SELECT u FROM Usuario u WHERE u.dataBloqueio = null order by u.dataCriacao desc")
    List<Usuario> findAll();

    /**
     * @param oid
     * @return lista de clientes com primeiro nome igual ao informado
     */
    @Query("SELECT u FROM Usuario u WHERE u.oid = ?1")
    Usuario findByOid(String oid);

    Usuario findByLogin(String login);

    @Query("SELECT u FROM Usuario u WHERE u.login = ?1 and u.senha = ?2")
    Usuario findByLoginAndSenha(String login, String senha);

}
