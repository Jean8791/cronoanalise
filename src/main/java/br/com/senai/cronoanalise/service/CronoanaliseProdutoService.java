package br.com.senai.cronoanalise.service;

import br.com.senai.cronoanalise.dto.CronoanaliseDto;
import br.com.senai.cronoanalise.models.CronoanaliseProduto;
import br.com.senai.cronoanalise.models.CronoanaliseProdutoOperacao;
import br.com.senai.cronoanalise.repository.CronoanaliseProdutoRepository;
import br.com.senai.cronoanalise.utils.UtilColecao;
import br.com.senai.cronoanalise.utils.UtilString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CronoanaliseProdutoService {

    @Autowired
    private CronoanaliseProdutoEmpresaService cronoanaliseProdutoEmpresaService;

    @Autowired
    private CronoanaliseProdutoRepository cronoanaliseProdutoRepository;

    @Autowired
    private CronoanaliseProdutoOperacaoService cronoanaliseProdutoOperacaoService;

    public CronoanaliseProduto montarProdutoCronoanalise(CronoanaliseDto.Produto cronoProduto) {
        CronoanaliseProduto produto = new CronoanaliseProduto();
        if (UtilString.stringValida(cronoProduto.getNome())) {
            produto.setNome(cronoProduto.getNome());
        }
        if (UtilString.stringValida(cronoProduto.getCodigo())) {
            produto.setCodigo(cronoProduto.getCodigo());
        }
        if (cronoProduto.getEmpresa() != null) {
            produto.setEmpresa(cronoanaliseProdutoEmpresaService.montaCronoanaliseProdutoEmpresa(cronoProduto.getEmpresa()));
        }
        CronoanaliseProduto produtoSalvo = salvar(produto);

        if (UtilColecao.colecaoValida(cronoProduto.getOperacoes())) {
            List<CronoanaliseProdutoOperacao> listOperacaoSalva = cronoanaliseProdutoOperacaoService.montarOperacoesCronoanalise(cronoProduto.getOperacoes());
            if (UtilColecao.colecaoValida(listOperacaoSalva)) {
                produto.setOperacoes(listOperacaoSalva);
            }
        }
        return salvar(produtoSalvo);
    }

    public CronoanaliseDto.Produto montarProdutoCronoanaliseDto(CronoanaliseProduto cronoanaliseProduto) {
        CronoanaliseDto.Produto produto = new CronoanaliseDto.Produto();
        if (cronoanaliseProduto != null) {
            if (UtilString.stringValida(cronoanaliseProduto.getNome())) {
                produto.setNome(cronoanaliseProduto.getNome());
            }
            if (UtilString.stringValida(cronoanaliseProduto.getCodigo())) {
                produto.setCodigo(cronoanaliseProduto.getCodigo());
            }
            if (cronoanaliseProduto.getEmpresa() != null && UtilString.stringValida(cronoanaliseProduto.getEmpresa().getNome())) {
                CronoanaliseDto.Empresa empresa = new CronoanaliseDto.Empresa();
                empresa.setOid(cronoanaliseProduto.getEmpresa().getOid());
                empresa.setNome(cronoanaliseProduto.getEmpresa().getNome());
//                empresa.setTurno(UtilString.convertTimeToString(cronoanaliseProduto.getEmpresa().getTurno()));
                produto.setEmpresa(empresa);
            }
            if (cronoanaliseProduto.getOperacoes() != null) {
                produto.setOperacoes(cronoanaliseProdutoOperacaoService.montarOperacoesCronoanaliseDto(cronoanaliseProduto.getOperacoes()));
            }
        }
        return produto;
    }

    public CronoanaliseProduto salvar(CronoanaliseProduto produto) {
        return cronoanaliseProdutoRepository.save(produto);
    }
}
