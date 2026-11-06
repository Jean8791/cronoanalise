package br.com.senai.cronoanalise.cronoanalise;

import br.com.senai.cronoanalise.cronoanalise.CronoanaliseDtos.CorrigirRequest;
import br.com.senai.cronoanalise.cronoanalise.CronoanaliseDtos.CriarRequest;
import br.com.senai.cronoanalise.cronoanalise.CronoanaliseDtos.Detalhe;
import br.com.senai.cronoanalise.cronoanalise.CronoanaliseDtos.Resumo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cronoanalises")
@Tag(name = "Cronoanálises", description = "Registro dos tempos cronometrados e resultado do cálculo")
public class CronoanaliseController {

    private final CronoanaliseService service;

    public CronoanaliseController(CronoanaliseService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar cronoanálises (busca por produto, empresa ou cronoanalista)")
    public List<Resumo> listar(@RequestParam(required = false) String busca) {
        return service.listar(busca);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar cronoanálise com o resultado do cálculo",
            description = "Médias, amplitude, d2, z, ciclos necessários, tempo normal, fator de tolerância, tempo padrão e peças por hora e por turno.")
    public Detalhe detalhar(@PathVariable UUID id) {
        return service.detalhar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar cronoanálise de um produto")
    public Detalhe criar(@Valid @RequestBody CriarRequest req) {
        return service.criar(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Corrigir tempos, cronoanalista ou erro relativo (autor, ADMIN ou PROFESSOR)")
    public Detalhe corrigir(@PathVariable UUID id, @Valid @RequestBody CorrigirRequest req) {
        return service.corrigir(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir cronoanálise (autor, ADMIN ou PROFESSOR)")
    public void excluir(@PathVariable UUID id) {
        service.excluir(id);
    }
}
