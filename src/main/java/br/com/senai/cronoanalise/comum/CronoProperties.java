package br.com.senai.cronoanalise.comum;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@ConfigurationProperties(prefix = "crono")
public record CronoProperties(Jwt jwt, Admin admin, Demo demo, Login login, List<String> corsOrigens) {

    public record Jwt(String segredo, Duration validade) {
    }

    public record Admin(String login, String senha) {

        public Admin {
            login = limpar(login);
            senha = limpar(senha);
        }
    }

    public record Demo(String login, String senha) {

        public Demo {
            login = limpar(login);
            senha = limpar(senha);
        }

        public boolean ativo() {
            return login != null && !login.isBlank() && senha != null && !senha.isBlank();
        }

        public boolean ehDemo(String outroLogin) {
            return ativo() && login.equalsIgnoreCase(outroLogin);
        }
    }

    public record Login(int limiteTentativas, Duration janela, String cabecalhoIp) {
    }

    private static String limpar(String valor) {
        return valor == null ? null : valor.strip();
    }
}
