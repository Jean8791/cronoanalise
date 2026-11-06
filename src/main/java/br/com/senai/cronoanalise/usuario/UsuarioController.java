package br.com.senai.cronoanalise.usuario;

import br.com.senai.cronoanalise.usuario.UsuarioDtos.UsuarioRequest;
import br.com.senai.cronoanalise.usuario.UsuarioDtos.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Usuários", description = "Somente o perfil ADMIN")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar usuários")
    public List<UsuarioResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário")
    public UsuarioResponse buscar(@PathVariable UUID id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar usuário")
    public UsuarioResponse criar(@Valid @RequestBody UsuarioRequest req) {
        return service.criar(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário (senha opcional)")
    public UsuarioResponse atualizar(@PathVariable UUID id, @Valid @RequestBody UsuarioRequest req) {
        return service.atualizar(id, req);
    }

    @PostMapping("/{id}/bloquear")
    @Operation(summary = "Bloquear usuário")
    public UsuarioResponse bloquear(@PathVariable UUID id) {
        return service.bloquear(id);
    }

    @PostMapping("/{id}/desbloquear")
    @Operation(summary = "Desbloquear usuário")
    public UsuarioResponse desbloquear(@PathVariable UUID id) {
        return service.desbloquear(id);
    }
}
