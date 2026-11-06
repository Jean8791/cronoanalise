package br.com.senai.cronoanalise.auth;

import br.com.senai.cronoanalise.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class UsuarioAtivoFilter extends OncePerRequestFilter {

    private final UsuarioRepository usuarios;

    public UsuarioAtivoFilter(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken token) {
            boolean ativo = usuarios.findById(UUID.fromString(token.getToken().getSubject()))
                    .map(u -> !u.isBloqueado())
                    .orElse(false);
            if (!ativo) {
                SecurityContextHolder.clearContext();
                res.setStatus(HttpStatus.UNAUTHORIZED.value());
                res.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
                res.getWriter().write("{\"title\":\"Unauthorized\",\"status\":401,\"detail\":\"Usuário bloqueado ou inexistente.\"}");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
