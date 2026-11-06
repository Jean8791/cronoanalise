package br.com.senai.cronoanalise.empresa;

import br.com.senai.cronoanalise.empresa.EmpresaDtos.EmpresaRequest;
import br.com.senai.cronoanalise.empresa.EmpresaDtos.EmpresaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/empresas")
@Tag(name = "Empresas", description = "Leitura para todos; alteração para ADMIN e PROFESSOR")
public class EmpresaController {

    private static final String GESTOR = "hasAnyRole('ADMIN','PROFESSOR')";

    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar empresas (busca por nome ou CNPJ)")
    public List<EmpresaResponse> listar(@RequestParam(required = false) String busca,
                                        @RequestParam(defaultValue = "false") boolean incluirBloqueadas) {
        return service.listar(busca, incluirBloqueadas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empresa")
    public EmpresaResponse buscar(@PathVariable UUID id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(GESTOR)
    @Operation(summary = "Criar empresa com turno e paradas")
    public EmpresaResponse criar(@Valid @RequestBody EmpresaRequest req) {
        return service.criar(req);
    }

    @PutMapping("/{id}")
    @PreAuthorize(GESTOR)
    @Operation(summary = "Atualizar empresa")
    public EmpresaResponse atualizar(@PathVariable UUID id, @Valid @RequestBody EmpresaRequest req) {
        return service.atualizar(id, req);
    }

    @PostMapping("/{id}/bloquear")
    @PreAuthorize(GESTOR)
    @Operation(summary = "Bloquear empresa")
    public EmpresaResponse bloquear(@PathVariable UUID id) {
        return service.bloquear(id, true);
    }

    @PostMapping("/{id}/desbloquear")
    @PreAuthorize(GESTOR)
    @Operation(summary = "Desbloquear empresa")
    public EmpresaResponse desbloquear(@PathVariable UUID id) {
        return service.bloquear(id, false);
    }
}
