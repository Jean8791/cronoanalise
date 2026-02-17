package br.com.senai.cronoanalise.resources;

import br.com.senai.cronoanalise.service.ZoomProdutoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api")
@Api(value = "API REST Zoom de Produtos")
public class ZoomProdutoResource {

    @Autowired
    private ZoomProdutoService zoomProdutoService;

    @ApiOperation(value = "Retorna uma lista de Produtos")
    @GetMapping("/zoom-produtos")
    public ResponseEntity<?> listaProdutos() {
        return new ResponseEntity<>(zoomProdutoService.findAllProdutos(), HttpStatus.OK);
    }
}
