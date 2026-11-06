package br.com.senai.cronoanalise.auth;

import br.com.senai.cronoanalise.usuario.Perfil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UsuarioLogado {

    public UUID id() {
        return UUID.fromString(token().getToken().getSubject());
    }

    public Perfil perfil() {
        return Perfil.valueOf(token().getToken().getClaimAsString("perfil"));
    }

    public boolean podeGerenciarCadastros() {
        Perfil p = perfil();
        return p == Perfil.ADMIN || p == Perfil.PROFESSOR;
    }

    private JwtAuthenticationToken token() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken t) {
            return t;
        }
        throw new IllegalStateException("Requisição sem usuário autenticado.");
    }
}
