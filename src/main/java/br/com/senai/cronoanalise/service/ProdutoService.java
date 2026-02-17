package br.com.senai.cronoanalise.service;

import br.com.senai.cronoanalise.dto.CronoanaliseDto;
import br.com.senai.cronoanalise.dto.EmpresaDto;
import br.com.senai.cronoanalise.dto.OperacaoDto;
import br.com.senai.cronoanalise.dto.ProdutoDto;
import br.com.senai.cronoanalise.models.Empresa;
import br.com.senai.cronoanalise.models.Operacao;
import br.com.senai.cronoanalise.models.Produto;
import br.com.senai.cronoanalise.repository.EmpresaRepository;
import br.com.senai.cronoanalise.repository.ProdutoRepository;
import br.com.senai.cronoanalise.utils.UtilColecao;
import br.com.senai.cronoanalise.utils.UtilData;
import br.com.senai.cronoanalise.utils.UtilString;
import br.com.senai.cronoanalise.utils.UtilValor;
import exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private OperacaoService operacaoService;

    public List<ProdutoDto> findAllProdutos() {
        List<ProdutoDto> produtoDtos = new ArrayList<>();
        List<Produto> produtos = produtoRepository.findAllNaoBloqueados();
        if (UtilColecao.listaValida(produtos)) {
            for (Produto produto : produtos) {
                ProdutoDto produtoDto = new ProdutoDto();
                produtoDto.setOid(produto.getOid());
                produtoDto.setNome(produto.getNome());
                produtoDto.setCodigo(produto.getCodigo());
                produtoDto.setDataCriacao(UtilData.retornaDDMMYYYY(Timestamp.valueOf(produto.getDataCriacao())));
                if (produto.getEmpresa() != null) {
                    produtoDto.setEmpresa(new EmpresaDto());
                    produtoDto.getEmpresa().setNome(produto.getEmpresa().getNome());
                    produtoDto.getEmpresa().setOid(produto.getEmpresa().getOid());
                    produtoDto.getEmpresa().setCnpj(produto.getEmpresa().getCnpj());
                    produtoDto.getEmpresa().setDataCriacao(UtilData.retornaDDMMYYYY(Timestamp.valueOf(produto.getEmpresa().getDataCriacao())));
                    produtoDto.getEmpresa().setFatorVelocidade(produto.getEmpresa().getFatorVelocidade().toString());
                }
                produtoDtos.add(produtoDto);
            }
        }
        return produtoDtos;
    }

    public String salvar(ProdutoDto produtoDto) {
        Produto produto = new Produto();
        produto.setNome(produtoDto.getNome());
        produto.setCodigo(produtoDto.getCodigo());
        produto.setEmpresa(empresaRepository.findByOid(produtoDto.getEmpresa().getOid()));
        if (UtilColecao.listaValida(produtoDto.getOperacoes())) {
            List<Operacao> listOperacao = new ArrayList<>();
            for (OperacaoDto operacaoDto : produtoDto.getOperacoes()) {
                Operacao operacao = new Operacao();
                operacao.setNome(operacaoDto.getNome());
                operacao.setDescricao(operacaoDto.getDescricao());
                operacao.setQuantidadeOperadores(UtilValor.retornaInteger(operacaoDto.getQuantidadeOperadores()));
                listOperacao.add(operacao);
            }
            produto.setOperacoes(listOperacao);
        }
        salvarProduto(produto);
        return "Produto " + produto.getNome() + " foi salvo com sucesso!";
    }

    public String atualizar(ProdutoDto produtoDto) {
        Produto produto = produtoRepository.findByOid(produtoDto.getOid());
        produto.setNome(produtoDto.getNome());
        produto.setCodigo(produtoDto.getCodigo());
        Empresa empresa = empresaRepository.findByOid(produtoDto.getEmpresa().getOid());
        if (empresa != null) {
            produto.setEmpresa(empresa);
        }
        if (UtilColecao.listaValida(produtoDto.getOperacoes())) {
            List<Operacao> listOperacao = new ArrayList<>();
            for (OperacaoDto operacaoDto : produtoDto.getOperacoes()) {
                Operacao operacao = new Operacao();
                operacao.setOid(operacaoDto.getOid());
                operacao.setNome(operacaoDto.getNome());
                operacao.setDescricao(operacaoDto.getDescricao());
                operacao.setQuantidadeOperadores(UtilValor.retornaInteger(operacaoDto.getQuantidadeOperadores()));
                listOperacao.add(operacao);
            }
            produto.setOperacoes(listOperacao);
        }
        atualizarProduto(produto);
        return "Produto " + produto.getNome() + " foi atualizado com sucesso!";
    }

    public Produto montarProdutoCronoanalise(CronoanaliseDto.Produto cronoProduto) {
        Produto produto = new Produto();
        if (UtilString.stringValida(cronoProduto.getNome())) {
            produto.setNome(cronoProduto.getNome());
        }
        if (UtilString.stringValida(cronoProduto.getCodigo())) {
            produto.setCodigo(cronoProduto.getCodigo());
        }
        if (cronoProduto.getEmpresa() != null) {
            Empresa empresa = empresaRepository.findByOid(cronoProduto.getEmpresa().getOid());
            if (empresa != null) {
                produto.setEmpresa(empresa);
            }
        }
        Produto produtoSalvo = salvarProduto(produto);

        if (UtilColecao.colecaoValida(cronoProduto.getOperacoes())) {
            List<Operacao> listOperacaoSalva = operacaoService.montarOperacoesCronoanalise(cronoProduto.getOperacoes(), produtoSalvo);
            if (UtilColecao.colecaoValida(listOperacaoSalva)) {
                produto.setOperacoes(listOperacaoSalva);
            }
        }
        return salvarProduto(produtoSalvo);
    }

    private Produto salvarProduto(Produto produto) {
        validaProduto(produto);
        return produtoRepository.save(produto);
    }

    private void atualizarProduto(Produto produto) {
        validaProduto(produto);
        produtoRepository.save(produto);
    }

    public Produto bloquear(String oid) {
        Produto produto = produtoRepository.findByOid(oid);

        if (produto == null) {
            throw new ResourceNotFoundException("Produto não econtrado.");
        }
        produto.setDataBloqueio(LocalDateTime.now());
        produto = produtoRepository.save(produto);

        return produto;
    }

    private void validaProduto(Produto produto) {

        if (produto == null) {
            throw new ResourceNotFoundException("Erro ao salvar um produto.");
        }

        if (!UtilString.stringValida(produto.getNome())) {
            throw new ResourceNotFoundException("Nome é obrigatório");
        }

        if (!UtilString.stringValida(produto.getCodigo())) {
            throw new ResourceNotFoundException("Código é obrigatório");
        }

        if (produto.getEmpresa() == null) {
            throw new ResourceNotFoundException("Empresa é obrigatório");
        }
    }
}
