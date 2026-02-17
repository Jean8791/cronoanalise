package br.com.senai.cronoanalise.resources;

import br.com.senai.cronoanalise.dto.ProdutoDto;
import br.com.senai.cronoanalise.models.Produto;
import br.com.senai.cronoanalise.repository.ProdutoRepository;
import br.com.senai.cronoanalise.service.ProdutoService;
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
@Api(value = "API REST Produtos")
public class ProdutoResource {

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ProdutoService produtoService;

    @ApiOperation(value = "Retorna uma lista de Produtos")
    @GetMapping("/produtos")
    public ResponseEntity<?> listaProdutos() {
        return new ResponseEntity<>(produtoService.findAllProdutos(), HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna uma produto único")
    @GetMapping("/produto/{oid}")
    public ResponseEntity<?> listaProdutoUnico(@PathVariable(value = "oid") String oid) {
        return new ResponseEntity<>(produtoRepository.findByOid(oid), HttpStatus.OK);
    }

    @ApiOperation(value = "Salva um produto")
    @PostMapping("/produto")
    public ResponseEntity<?> post(@RequestBody @Valid ProdutoDto produtoDto) {
        if (!UtilString.stringValida(produtoDto.getOid())) {
            return new ResponseEntity<>(produtoService.salvar(produtoDto), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(produtoService.atualizar(produtoDto), HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Deleta um produto")
    @DeleteMapping("/produto")
    public void deletaProduto(@RequestBody @Valid Produto produto) {
        produtoRepository.delete(produto);
    }

    @ApiOperation(value = "Bloquear um produto")
    @RequestMapping(value = "/produto-bloquear", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> bloquear(@RequestBody @Valid ProdutoDto produtoDto) {
        Produto produto = produtoService.bloquear(produtoDto.getOid());
        return new ResponseEntity<>("Produto " + produto.getNome() + " deletado com sucesso!", HttpStatus.OK);
    }
}
