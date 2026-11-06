package br.com.senai.cronoanalise.usuario;

import br.com.senai.cronoanalise.comum.EntidadeAuditada;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "usuario")
public class Usuario extends EntidadeAuditada {

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 160)
    private String email;

    @Column(nullable = false, length = 60, unique = true)
    private String login;

    @Column(name = "senha_hash", nullable = false, length = 100)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Perfil perfil;

    @Column(name = "bloqueado_em")
    private Instant bloqueadoEm;

    protected Usuario() {
    }

    public Usuario(String nome, String email, String login, String senhaHash, Perfil perfil) {
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
    }

    public boolean isBloqueado() {
        return bloqueadoEm != null;
    }

    public void bloquear() {
        if (bloqueadoEm == null) bloqueadoEm = Instant.now();
    }

    public void desbloquear() {
        bloqueadoEm = null;
    }

    public void atualizar(String nome, String email, String login, Perfil perfil) {
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.perfil = perfil;
    }

    public void trocarSenha(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public Instant getBloqueadoEm() {
        return bloqueadoEm;
    }
}
