package br.com.senai.cronoanalise.produto;

import br.com.senai.cronoanalise.produto.ProdutoDtos.ProdutoRequest;
import br.com.senai.cronoanalise.produto.ProdutoDtos.ProdutoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Leitura para todos; alteração para ADMIN e PROFESSOR")
public class ProdutoController {

    private static final String GESTOR = "hasAnyRole('ADMIN','PROFESSOR')";

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar produtos (busca por nome ou código, filtro por empresa)")
    public List<ProdutoResponse> listar(@RequestParam(required = false) String busca,
                                        @RequestParam(required = false) UUID empresaId,
                                        @RequestParam(defaultValue = "false") boolean incluirBloqueados) {
        return service.listar(busca, empresaId, incluirBloqueados);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto com as operações")
    public ProdutoResponse buscar(@PathVariable UUID id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(GESTOR)
    @Operation(summary = "Criar produto com as operações em ordem")
    public ProdutoResponse criar(@Valid @RequestBody ProdutoRequest req) {
        return service.criar(req);
    }

    @PutMapping("/{id}")
    @PreAuthorize(GESTOR)
    @Operation(summary = "Atualizar produto")
    public ProdutoResponse atualizar(@PathVariable UUID id, @Valid @RequestBody ProdutoRequest req) {
        return service.atualizar(id, req);
    }

    @PostMapping("/{id}/bloquear")
    @PreAuthorize(GESTOR)
    @Operation(summary = "Bloquear produto")
    public ProdutoResponse bloquear(@PathVariable UUID id) {
        return service.bloquear(id, true);
    }

    @PostMapping("/{id}/desbloquear")
    @PreAuthorize(GESTOR)
    @Operation(summary = "Desbloquear produto")
    public ProdutoResponse desbloquear(@PathVariable UUID id) {
        return service.bloquear(id, false);
    }
}
