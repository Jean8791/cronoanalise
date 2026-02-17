package br.com.senai.cronoanalise.resources;

import br.com.senai.cronoanalise.dto.CronoanaliseDto;
import br.com.senai.cronoanalise.models.Cronoanalise;
import br.com.senai.cronoanalise.service.CronoanaliseService;
import br.com.senai.cronoanalise.utils.UtilString;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api")
@Api(value = "API REST Cronoanalise")
public class CronoanaliseResource {

    @Autowired
    private CronoanaliseService cronoanaliseService;

    @ApiOperation(value = "Retorna uma lista de Cronoanalise")
    @GetMapping("/cronoanalises")
    public ResponseEntity<?> listaCronoanalise() {
        return new ResponseEntity<>(cronoanaliseService.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Salva uma Cronoanalise")
    @PostMapping("/cronoanalise")
    public ResponseEntity<?> post(@RequestBody @Valid CronoanaliseDto cronoanaliseDto) {
        return new ResponseEntity<>(cronoanaliseService.montarCronoanalise(cronoanaliseDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna uma lista de Cronoanalise")
    @GetMapping("/cronoanalise-detalhar/{oid}")
    public ResponseEntity<?> detalhaCronoanalise(@PathVariable(value = "oid") String oid) {
        return new ResponseEntity<>(cronoanaliseService.detalhaCronoanalise(oid), HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna o ultimo registro")
    @GetMapping("/cronoanalise-ultimo")
    public ResponseEntity<?> cronoanaliseUltimoAdicionado() {
        return new ResponseEntity<>(cronoanaliseService.findUltimoRegistro(), HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna uma Cronoanalise")
    @GetMapping("/cronoanalise/{oid}")
    public ResponseEntity<?> getCronoanalise(@PathVariable(value = "oid") String oid) {
        return new ResponseEntity<>(cronoanaliseService.getCronoanaliseDto(oid), HttpStatus.OK);
    }
}
