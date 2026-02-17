package br.com.senai.cronoanalise.autenticacao.controller;

import br.com.senai.cronoanalise.autenticacao.config.JwtTokenUtil;
import br.com.senai.cronoanalise.autenticacao.service.JwtUserDetailsService;
import br.com.senai.cronoanalise.models.JwtRequest;
import br.com.senai.cronoanalise.models.Usuario;
import br.com.senai.cronoanalise.service.UsuarioService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UsuarioService usuarioService;

    private RetornoUsuarioLogado retornoUsuarioLogado = new RetornoUsuarioLogado();

    @RequestMapping(value = "authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        retornoUsuarioLogado.setToken(jwtTokenUtil.generateToken(userDetails));
        return ResponseEntity.ok(new JwtResponse(retornoUsuarioLogado));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            Usuario autenticado = usuarioService.autenticarUsuario(username, password);
            if (autenticado != null) {
                password = "password";
                retornoUsuarioLogado.setNome(autenticado.getNome());
                retornoUsuarioLogado.setPermissao(autenticado.getTipoUsuario().getDescricao());
            } else {
                password = null;
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public static class RetornoUsuarioLogado {
        private String token;
        private String nome;
        private String permissao;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getPermissao() {
            return permissao;
        }

        public void setPermissao(String permissao) {
            this.permissao = permissao;
        }
    }
}