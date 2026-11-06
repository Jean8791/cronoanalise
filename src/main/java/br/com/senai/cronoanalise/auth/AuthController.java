package br.com.senai.cronoanalise.auth;

import br.com.senai.cronoanalise.comum.NaoEncontradoException;
import br.com.senai.cronoanalise.comum.RegraNegocioException;
import br.com.senai.cronoanalise.usuario.Perfil;
import br.com.senai.cronoanalise.usuario.Usuario;
import br.com.senai.cronoanalise.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação")
public class AuthController {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final TokenService tokens;
    private final UsuarioLogado logado;
    private final String hashFalso;

    public AuthController(UsuarioRepository usuarios, PasswordEncoder encoder, TokenService tokens, UsuarioLogado logado) {
        this.usuarios = usuarios;
        this.encoder = encoder;
        this.tokens = tokens;
        this.logado = logado;
        this.hashFalso = encoder.encode(UUID.randomUUID().toString());
    }

    @PostMapping("/login")
    @Transactional(readOnly = true)
    @Operation(summary = "Entrar com login e senha", description = "Devolve o token JWT (Bearer) e os dados do usuário.")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        Usuario usuario = usuarios.findByLoginIgnoreCase(req.login().trim()).orElse(null);
        boolean senhaOk = encoder.matches(req.senha(), usuario != null ? usuario.getSenhaHash() : hashFalso);
        if (usuario == null || !senhaOk) {
            throw new RegraNegocioException(HttpStatus.UNAUTHORIZED, "Login ou senha inválidos.");
        }
        if (usuario.isBloqueado()) {
            throw new RegraNegocioException(HttpStatus.FORBIDDEN, "Usuário bloqueado.");
        }
        TokenService.Token token = tokens.gerar(usuario);
        return new LoginResponse(token.valor(), token.expiraEm(), Eu.de(usuario));
    }

    @GetMapping("/eu")
    @Transactional(readOnly = true)
    @Operation(summary = "Dados do usuário logado")
    public Eu eu() {
        return usuarios.findById(logado.id()).map(Eu::de)
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado."));
    }

    public record LoginRequest(@NotBlank(message = "Informe o login.") String login,
                               @NotBlank(message = "Informe a senha.") String senha) {
    }

    public record LoginResponse(String token, Instant expiraEm, Eu usuario) {
    }

    public record Eu(UUID id, String nome, String login, Perfil perfil) {
        static Eu de(Usuario u) {
            return new Eu(u.getId(), u.getNome(), u.getLogin(), u.getPerfil());
        }
    }
}
