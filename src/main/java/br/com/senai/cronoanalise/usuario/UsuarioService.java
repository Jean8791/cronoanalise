package br.com.senai.cronoanalise.usuario;

import br.com.senai.cronoanalise.auth.UsuarioLogado;
import br.com.senai.cronoanalise.comum.CronoProperties;
import br.com.senai.cronoanalise.comum.NaoEncontradoException;
import br.com.senai.cronoanalise.comum.RegraNegocioException;
import br.com.senai.cronoanalise.usuario.UsuarioDtos.UsuarioRequest;
import br.com.senai.cronoanalise.usuario.UsuarioDtos.UsuarioResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final UsuarioLogado logado;
    private final CronoProperties props;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder, UsuarioLogado logado, CronoProperties props) {
        this.repo = repo;
        this.encoder = encoder;
        this.logado = logado;
        this.props = props;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return repo.findAllByOrderByNomeAsc().stream().map(UsuarioResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscar(UUID id) {
        return UsuarioResponse.de(carregar(id));
    }

    @Transactional
    public UsuarioResponse criar(UsuarioRequest req) {
        if (req.senha() == null || req.senha().isBlank()) {
            throw new RegraNegocioException("Informe a senha do novo usuário.");
        }
        if (repo.existsByLoginIgnoreCase(req.login())) {
            throw new RegraNegocioException(HttpStatus.CONFLICT, "Já existe um usuário com este login.");
        }
        Usuario u = new Usuario(req.nome().trim(), vazioParaNulo(req.email()), req.login().trim().toLowerCase(),
                encoder.encode(req.senha()), req.perfil());
        return UsuarioResponse.de(repo.save(u));
    }

    @Transactional
    public UsuarioResponse atualizar(UUID id, UsuarioRequest req) {
        Usuario u = carregar(id);
        protegerDemo(u);
        if (repo.existsByLoginIgnoreCaseAndIdNot(req.login(), id)) {
            throw new RegraNegocioException(HttpStatus.CONFLICT, "Já existe um usuário com este login.");
        }
        if (u.getId().equals(logado.id()) && req.perfil() != u.getPerfil()) {
            throw new RegraNegocioException("Você não pode alterar o próprio perfil.");
        }
        u.atualizar(req.nome().trim(), vazioParaNulo(req.email()), req.login().trim().toLowerCase(), req.perfil());
        if (req.senha() != null && !req.senha().isBlank()) {
            u.trocarSenha(encoder.encode(req.senha()));
        }
        return UsuarioResponse.de(u);
    }

    @Transactional
    public UsuarioResponse bloquear(UUID id) {
        Usuario u = carregar(id);
        protegerDemo(u);
        if (u.getId().equals(logado.id())) {
            throw new RegraNegocioException("Você não pode bloquear o próprio usuário.");
        }
        u.bloquear();
        return UsuarioResponse.de(u);
    }

    @Transactional
    public UsuarioResponse desbloquear(UUID id) {
        Usuario u = carregar(id);
        u.desbloquear();
        return UsuarioResponse.de(u);
    }

    private Usuario carregar(UUID id) {
        return repo.findById(id).orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado."));
    }

    private void protegerDemo(Usuario u) {
        if (props.demo() != null && props.demo().ehDemo(u.getLogin())) {
            throw new RegraNegocioException(HttpStatus.FORBIDDEN, "O usuário de demonstração não pode ser alterado.");
        }
    }

    private static String vazioParaNulo(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}
