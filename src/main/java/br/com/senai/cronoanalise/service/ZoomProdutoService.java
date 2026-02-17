package br.com.senai.cronoanalise.service;

import br.com.senai.cronoanalise.models.Produto;
import br.com.senai.cronoanalise.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ZoomProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> findAllProdutos() {
        return produtoRepository.findAllNaoBloqueados();
    }
}
