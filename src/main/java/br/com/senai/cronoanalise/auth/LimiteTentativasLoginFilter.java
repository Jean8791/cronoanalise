package br.com.senai.cronoanalise.auth;

import br.com.senai.cronoanalise.comum.CronoProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LimiteTentativasLoginFilter extends OncePerRequestFilter {

    private static final int LIMPEZA_ACIMA_DE = 10_000;

    private final Map<String, Janela> janelas = new ConcurrentHashMap<>();
    private final CronoProperties.Login config;

    public LimiteTentativasLoginFilter(CronoProperties props) {
        this.config = props.login();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        return !("POST".equals(req.getMethod()) && "/api/auth/login".equals(req.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        Instant agora = Instant.now();
        if (janelas.size() > LIMPEZA_ACIMA_DE) {
            janelas.values().removeIf(j -> j.expirou(agora));
        }
        Janela janela = janelas.compute(ip(req), (k, atual) ->
                atual == null || atual.expirou(agora) ? new Janela(agora.plus(config.janela()), 1) : atual.mais());
        if (janela.tentativas() > config.limiteTentativas()) {
            long segundos = Math.max(1, Duration.between(agora, janela.expiraEm()).toSeconds());
            res.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            res.setHeader("Retry-After", String.valueOf(segundos));
            res.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
            res.getWriter().write("{\"title\":\"Too Many Requests\",\"status\":429,\"detail\":\"Muitas tentativas de login. Tente novamente em alguns minutos.\"}");
            return;
        }
        chain.doFilter(req, res);
    }

    private String ip(HttpServletRequest req) {
        String cabecalho = config.cabecalhoIp();
        if (cabecalho != null && !cabecalho.isBlank()) {
            String valor = req.getHeader(cabecalho);
            if (valor != null && !valor.isBlank()) return valor.split(",")[0].trim();
        }
        return req.getRemoteAddr();
    }

    private record Janela(Instant expiraEm, int tentativas) {
        boolean expirou(Instant agora) {
            return !agora.isBefore(expiraEm);
        }

        Janela mais() {
            return new Janela(expiraEm, tentativas + 1);
        }
    }
}
