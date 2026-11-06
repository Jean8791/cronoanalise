package br.com.senai.cronoanalise.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public final class UsuarioDtos {

    private UsuarioDtos() {
    }

    public record UsuarioRequest(
            @NotBlank(message = "Informe o nome.") @Size(max = 120) String nome,
            @Email(message = "E-mail inválido.") @Size(max = 160) String email,
            @NotBlank(message = "Informe o login.")
            @Pattern(regexp = "[a-zA-Z0-9._-]{3,60}", message = "O login deve ter de 3 a 60 letras, números, ponto, hífen ou sublinhado.")
            String login,
            @Size(min = 8, max = 72, message = "A senha deve ter de 8 a 72 caracteres.") String senha,
            @NotNull(message = "Informe o perfil.") Perfil perfil
    ) {
    }

    public record UsuarioResponse(UUID id, String nome, String email, String login, Perfil perfil,
                                  boolean bloqueado, Instant criadoEm) {

        public static UsuarioResponse de(Usuario u) {
            return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getLogin(), u.getPerfil(),
                    u.isBloqueado(), u.getCriadoEm());
        }
    }
}
