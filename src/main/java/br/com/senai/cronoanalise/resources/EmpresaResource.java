package br.com.senai.cronoanalise.resources;

import br.com.senai.cronoanalise.dto.EmpresaDto;
import br.com.senai.cronoanalise.models.Empresa;
import br.com.senai.cronoanalise.repository.EmpresaRepository;
import br.com.senai.cronoanalise.service.EmpresaService;
import br.com.senai.cronoanalise.utils.UtilString;
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
@Api(value = "API REST Empresas")
public class EmpresaResource {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpresaService empresaService;

    @ApiOperation(value = "Retorna uma lista de Empresas")
    @GetMapping("/empresas")
    public ResponseEntity<?> listaEmpresas() {
        return new ResponseEntity<>(empresaService.findAllEmpresas(), HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna uma empresa unica")
    @GetMapping("/empresa/{oid}")
    public ResponseEntity<?> listaProdutoUnico(@PathVariable(value = "oid") String oid) {
        return new ResponseEntity<>(empresaService.findByOid(oid), HttpStatus.OK);
    }

    @ApiOperation(value = "Salva uma empresa")
    @PostMapping("/empresa")
    public ResponseEntity<?> post(@RequestBody @Valid EmpresaDto empresaDto) {
        if (!UtilString.stringValida(empresaDto.getOid())) {
            return new ResponseEntity<>(empresaService.salvar(empresaDto), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(empresaService.atualizar(empresaDto), HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Deleta uma Parada")
    @PostMapping("/empresa/{oid}/parada")
    public ResponseEntity<?> deletaEmpresa(@PathVariable(value = "oid") String oid) {
        return new ResponseEntity<>(empresaService.remover(oid), HttpStatus.OK);
    }

    @ApiOperation(value = "Bloquear uma empresa")
    @RequestMapping(value = "/empresa-bloquear", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> bloquear(@RequestBody @Valid EmpresaDto empresaDto) {
        Empresa empresa = empresaService.bloquear(empresaDto.getOid());
        return new ResponseEntity<>("Empresa " + empresa.getNome() + "  deletada com sucesso!", HttpStatus.OK);
    }
}
