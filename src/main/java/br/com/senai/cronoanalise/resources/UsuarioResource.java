package br.com.senai.cronoanalise.resources;

import br.com.senai.cronoanalise.dto.UsuarioDto;
import br.com.senai.cronoanalise.models.Usuario;
import br.com.senai.cronoanalise.repository.UsuarioRepository;
import br.com.senai.cronoanalise.service.UsuarioService;
import br.com.senai.cronoanalise.utils.UtilString;
import exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api")
@Api(value = "API REST Usuarios")
public class UsuarioResource {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @ApiOperation(value = "Retorna uma lista de Usuarios")
    @GetMapping("/usuarios")
    public ResponseEntity<?> listaUsuarios() throws Exception {
        try {
            return new ResponseEntity<>(usuarioService.findAllUsuarios(), HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("Erro ao Buscar Usuários.", e);
        }
    }

    @ApiOperation(value = "Salva um usuario")
    @PostMapping("/usuario")
    public ResponseEntity<?> post(@RequestBody @Valid UsuarioDto usuarioDto) throws Exception {
        try {
            if (!UtilString.stringValida(usuarioDto.getOid())) {
                return new ResponseEntity<>(usuarioService.salvar(usuarioDto), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(usuarioService.atualizar(usuarioDto), HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new Exception("Erro ao Cadastrar um Usuário.", e);
        }
    }

    @ApiOperation(value = "Retorna um usuario unico")
    @GetMapping("/usuario/{oid}")
    public ResponseEntity<?> listaProdutoUnico(@PathVariable(value = "oid") String oid) throws Exception {
        try {
            UsuarioDto usuarioDto = usuarioService.findByOid(oid);
            if (usuarioDto == null) {
                throw new ResourceNotFoundException("Usuário não encontrado.");
            }
            return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("Erro ao Buscar o Usuário.", e);
        }
    }

    @ApiOperation(value = "Deleta um produto")
    @DeleteMapping("/usuario")
    public ResponseEntity<?> deletaProduto(@RequestBody @Valid Usuario usuario) throws Exception {
        try {
            usuarioRepository.delete(usuario);
            return new ResponseEntity<>("Usuário Deletado com Sucesso.", HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("Erro ao Deletar um Usuário.");
        }
    }

    @ApiOperation(value = "Bloquear um usuario")
    @RequestMapping(value = "/usuario-bloquear", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> bloquear(@RequestBody UsuarioDto usuarioDto) throws Exception {
        try {
            Usuario usuario = usuarioService.bloquear(usuarioDto.getOid());
            return new ResponseEntity<>("Usuário " + usuario.getNome() + " deletado com sucesso!", HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("Erro ao Bloquear um Usuário.");
        }
    }

    @ApiOperation(value = "Get Usuario Logado")
    @PostMapping("/get-usuario-logado/{username}/{password}")
    public ResponseEntity<?> getUsuarioLogado(@PathVariable(value = "username") String username,
                                              @PathVariable(value = "password") String password) throws Exception {
        return new ResponseEntity<>(usuarioRepository.findByLoginAndSenha(username, password), HttpStatus.OK);
    }
}
