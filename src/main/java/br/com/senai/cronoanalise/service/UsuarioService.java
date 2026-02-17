package br.com.senai.cronoanalise.service;

import br.com.senai.cronoanalise.dto.UsuarioDto;
import br.com.senai.cronoanalise.models.Usuario;
import br.com.senai.cronoanalise.repository.UsuarioRepository;
import br.com.senai.cronoanalise.utils.UtilColecao;
import br.com.senai.cronoanalise.utils.UtilData;
import br.com.senai.cronoanalise.utils.UtilString;
import exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public List<UsuarioDto> findAllUsuarios() {
        List<UsuarioDto> usuarioDtos = new ArrayList<>();
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (UtilColecao.listaValida(usuarios)) {
            for (Usuario usuario : usuarios) {
                UsuarioDto usuarioDto = new UsuarioDto();
                usuarioDto.setOid(usuario.getOid());
                usuarioDto.setNome(usuario.getNome());
                usuarioDto.setEmail(usuario.getEmail());
                usuarioDto.setLogin(usuario.getLogin());
                usuarioDto.setSenha(usuario.getSenha());
                usuarioDto.setConfirmarSenha(usuario.getConfirmarSenha());
                usuarioDto.setTipoUsuario(usuario.getTipoUsuario().getDescricao());
                usuarioDto.setDataCriacao(UtilData.retornaDDMMYYYY(Timestamp.valueOf(usuario.getDataCriacao())));
                usuarioDtos.add(usuarioDto);
            }
        }
        return usuarioDtos;
    }

    public UsuarioDto findByOid(String oid) {
        Usuario usuario = usuarioRepository.findByOid(oid);
        UsuarioDto usuarioDto = new UsuarioDto();
        if (usuario != null) {
            usuarioDto.setOid(usuario.getOid());
            usuarioDto.setNome(usuario.getNome());
            usuarioDto.setEmail(usuario.getEmail());
            usuarioDto.setLogin(usuario.getLogin());
            usuarioDto.setSenha(usuario.getSenha());
            usuarioDto.setConfirmarSenha(usuario.getConfirmarSenha());
            usuarioDto.setTipoUsuario(usuario.getTipoUsuario().getDescricao());
            usuarioDto.setDataCriacao(UtilData.retornaDDMMYYYY(Timestamp.valueOf(usuario.getDataCriacao())));
        }
        return usuarioDto;
    }

    public String salvar(UsuarioDto usuarioDto) {
        validaUsuario(usuarioDto);
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDto.getNome());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setLogin(usuarioDto.getLogin());
        usuario.setSenha(usuarioDto.getSenha());
        usuario.setConfirmarSenha(usuarioDto.getConfirmarSenha());
        usuario.setTipoUsuario(converteTipoUsuario(usuarioDto.getTipoUsuario()));
        usuarioRepository.save(usuario);
        return "Usuário " + usuario.getNome() + " foi salvo com sucesso!";
    }

    public String atualizar(UsuarioDto usuarioDto) {
        validaUsuario(usuarioDto);
        Usuario usuario = usuarioRepository.findByOid(usuarioDto.getOid());
        usuario.setNome(usuarioDto.getNome());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setLogin(usuarioDto.getLogin());
        usuario.setTipoUsuario(converteTipoUsuario(usuarioDto.getTipoUsuario()));
        if (UtilString.stringValida(usuarioDto.getSenha()) && UtilString.stringValida(usuarioDto.getConfirmarSenha())) {
            usuario.setSenha(usuarioDto.getSenha());
            usuario.setConfirmarSenha(usuarioDto.getConfirmarSenha());
        }
        usuarioRepository.save(usuario);
        return "Usuário " + usuario.getNome() + " foi atualizado com sucesso!";
    }

    public Usuario autenticarUsuario(String login, String senha) {
        if (UtilString.stringValida(login) && UtilString.stringValida(senha)) {
            Usuario usuario = usuarioRepository.findByLoginAndSenha(login, senha);
            if (usuario == null) {
                return null;
            }
            return usuario;
        }
        return null;
    }

    public Usuario bloquear(String oid) {
        Usuario usuario = usuarioRepository.findByOid(oid);

        if (usuario == null) {
            throw new ResourceNotFoundException("Usuário não econtrado.");
        }
        usuario.setDataBloqueio(LocalDateTime.now());
        usuario = usuarioRepository.save(usuario);

        return usuario;
    }

    private void validaUsuario(UsuarioDto usuarioDto) {

        if (usuarioDto == null) {
            throw new ResourceNotFoundException("Erro ao salvar um usuário.");
        }

        if (!UtilString.stringValida(usuarioDto.getNome())) {
            throw new ResourceNotFoundException("Nome do usuário é obrigatório");
        }

        if (!UtilString.stringValida(usuarioDto.getLogin())) {
            throw new ResourceNotFoundException("Login do usuário é obrigatório");
        }

        if (!UtilString.stringValida(usuarioDto.getSenha()) && usuarioDto.getSenha().compareTo(usuarioDto.getConfirmarSenha()) > 0) {
            throw new ResourceNotFoundException("As senhas não conferem.");
        }

        if (usuarioDto.getTipoUsuario() == null) {
            throw new ResourceNotFoundException("Tipo de usuário é obrigatório");
        }
    }

    public Usuario.TipoUsuario converteTipoUsuario(String tipoUsuario) {
        switch (tipoUsuario) {
            case "Professor":
                return Usuario.TipoUsuario.PROFESSOR;
            case "Aluno":
                return Usuario.TipoUsuario.ALUNO;
        }
        return null;
    }
}
