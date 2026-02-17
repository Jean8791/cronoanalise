package br.com.senai.cronoanalise.resources;

import br.com.senai.cronoanalise.dto.MaquinaDto;
import br.com.senai.cronoanalise.dto.UsuarioDto;
import br.com.senai.cronoanalise.service.ZoomMaquinasService;
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
@Api(value = "API REST Maquinas")
public class ZoomMaquinasResource {

    @Autowired
    ZoomMaquinasService zoomMaquinasService;

    @ApiOperation(value = "Retorna uma lista de Maquinas")
    @GetMapping("/zoom-maquinas")
    public ResponseEntity<?> listaMaquinas() {
        return new ResponseEntity<>(zoomMaquinasService.findAllMaquinas(), HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna uma maquina")
    @GetMapping("/zoom-maquinas/{nome}")
    public ResponseEntity<?> buscaMaquina(@PathVariable(value = "nome") String nome) throws Exception {
        try {
            MaquinaDto maquinasDto = zoomMaquinasService.findByNome(nome);
            if (maquinasDto == null) {
                throw new ResourceNotFoundException("Maquina não encontrada.");
            }
            return new ResponseEntity<>(maquinasDto, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("Erro ao Buscar a Maquina.", e);
        }
    }
}
