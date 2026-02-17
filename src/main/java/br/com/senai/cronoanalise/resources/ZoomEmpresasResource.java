package br.com.senai.cronoanalise.resources;

import br.com.senai.cronoanalise.dto.EmpresaDto;
import br.com.senai.cronoanalise.service.ZoomEmpresasService;
import exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api")
@Api(value = "API REST Zoom de Empresas")
public class ZoomEmpresasResource {

    @Autowired
    ZoomEmpresasService zoomEmpresasService;

    @ApiOperation(value = "Retorna uma lista de Empresas")
    @GetMapping("/zoom-empresas")
    public ResponseEntity<?> listaEmpresas() {
        return new ResponseEntity<>(zoomEmpresasService.findAllEmpresas(), HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna uma empresa")
    @GetMapping("/zoom-empresas/{nome}")
    public ResponseEntity<?> buscaEmpresa(@PathVariable(value = "nome") String nome) throws Exception {
        try {
            EmpresaDto empresasDto = zoomEmpresasService.findByNome(nome);
            if (empresasDto == null) {
                throw new ResourceNotFoundException("Empresa não encontrada.");
            }
            return new ResponseEntity<>(empresasDto, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("Erro ao Buscar a Empresa.", e);
        }
    }

    @ApiOperation(value = "Retorna uma empresa")
    @GetMapping("/zoom-empresas-oid/{oid}")
    public ResponseEntity<?> buscaEmpresaOid(@PathVariable(value = "oid") String oid) throws Exception {
        try {
            EmpresaDto empresasDto = zoomEmpresasService.findByOid(oid);
            if (empresasDto == null) {
                throw new ResourceNotFoundException("Empresa não encontrada.");
            }
            return new ResponseEntity<>(empresasDto, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("Erro ao Buscar a Empresa.", e);
        }
    }
}
