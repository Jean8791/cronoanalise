package br.com.senai.cronoanalise.service;

import br.com.senai.cronoanalise.dto.CalculoDto;
import br.com.senai.cronoanalise.dto.CronoanaliseDto;
import br.com.senai.cronoanalise.dto.ProdutoDto;
import br.com.senai.cronoanalise.models.Cronoanalise;
import br.com.senai.cronoanalise.models.CronoanaliseProduto;
import br.com.senai.cronoanalise.models.CronoanaliseProdutoOperacao;
import br.com.senai.cronoanalise.models.Produto;
import br.com.senai.cronoanalise.repository.CronoanaliseProdutoOperacaoRepository;
import br.com.senai.cronoanalise.repository.CronoanaliseRepository;
import br.com.senai.cronoanalise.utils.UtilColecao;
import br.com.senai.cronoanalise.utils.UtilData;
import br.com.senai.cronoanalise.utils.UtilString;
import br.com.senai.cronoanalise.utils.UtilValor;
import exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CronoanaliseService {

    @Autowired
    private CronoanaliseRepository cronoanaliseRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CronoanaliseProdutoService cronoanaliseProdutoService;

    @Autowired
    private CalculoService calculoService;

    @Autowired
    private ObservacaoService observacaoService;

    @Autowired
    private CronoanaliseProdutoOperacaoRepository cronoanaliseProdutoOperacaoRepository;

    @Autowired
    private CronoanaliseProdutoOperacaoService cronoanaliseProdutoOperacaoService;

    public List<CronoanaliseDto> findAll() {
        List<Cronoanalise> listCronoanalise = cronoanaliseRepository.findAll();

        if (!UtilColecao.colecaoValida(listCronoanalise)) {
            return null;
        }

        return getCronoanalise(listCronoanalise);
    }

    public CalculoDto findUltimoRegistro() {
        Cronoanalise cronoanalise = cronoanaliseRepository.findUltimoAdicionado();

        if (cronoanalise == null) {
            return null;
        }

        CalculoDto calculoDto = calculoService.calcularCronoanalise(cronoanalise);
        return calculoDto;
    }


    public Cronoanalise salvar(Cronoanalise cronoanalise) {
        validaCronoanalise(cronoanalise);
        return cronoanaliseRepository.save(cronoanalise);
    }

    public CalculoDto detalhaCronoanalise(String oid) {
        Cronoanalise cronoanalise = cronoanaliseRepository.findByOid(oid);

        if (cronoanalise == null) {
            return null;
        }

        CalculoDto calculoDto = calculoService.calcularCronoanalise(cronoanalise);
        return calculoDto;
    }

    public List<CronoanaliseDto> getCronoanalise(List<Cronoanalise> listCronoanalise) {
        List<CronoanaliseDto> listCronoanaliseDto = new ArrayList<>();

        for (Cronoanalise cronoanalise : listCronoanalise) {

            CronoanaliseDto cronoanaliseDto = new CronoanaliseDto();

            if (UtilString.stringValida(cronoanalise.getOid())) {
                cronoanaliseDto.setOid(cronoanalise.getOid());
            }

            if (UtilString.stringValida(cronoanalise.getCronoanalista())) {
                cronoanaliseDto.setCronoanalista(cronoanalise.getCronoanalista());
            }

            cronoanaliseDto.setDataCriacao(UtilData.retornaDDMMYYYY(Timestamp.valueOf(cronoanalise.getDataCriacao())));

            if (cronoanalise.getProduto() != null) {

                CronoanaliseDto.Produto produto = new CronoanaliseDto.Produto();

                if (UtilString.stringValida(cronoanalise.getProduto().getCodigo())) {
                    produto.setCodigo(cronoanalise.getProduto().getCodigo());
                }

                if (UtilString.stringValida(cronoanalise.getProduto().getNome())) {
                    produto.setNome(cronoanalise.getProduto().getNome());
                }

                if (cronoanalise.getProduto().getEmpresa() != null) {
                    CronoanaliseDto.Empresa empresa = new CronoanaliseDto.Empresa();
                    empresa.setNome(cronoanalise.getProduto().getEmpresa().getNome());
                    produto.setEmpresa(empresa);
                }

                if (UtilColecao.colecaoValida(cronoanalise.getProduto().getOperacoes())) {
                    produto.setQuantidadeOperacoes(cronoanalise.getProduto().getOperacoes().size());
                    produto.setQuantidadeObservacoes(cronoanalise.getProduto().getOperacoes().get(0).getObservacoes().size());
                }

                cronoanaliseDto.setProduto(produto);

            }
            listCronoanaliseDto.add(cronoanaliseDto);
        }

        return listCronoanaliseDto;

    }

    public String montarCronoanalise(CronoanaliseDto cronoanaliseDto) {
        Cronoanalise cronoanaliseSalva = new Cronoanalise();
        if (cronoanaliseDto != null) {
            Cronoanalise cronoanalise = new Cronoanalise();
            if (!UtilString.stringValida(cronoanaliseDto.getOid())) {
                if (UtilString.stringValida(cronoanaliseDto.getCronoanalista())) {
                    cronoanalise.setCronoanalista(cronoanaliseDto.getCronoanalista());
                }
                if (UtilValor.valorValido(cronoanaliseDto.getErroRelativo())) {
                    cronoanalise.setErroRelativo(cronoanaliseDto.getErroRelativo());
                }
                if (cronoanaliseDto.getProduto() != null) {
                    if (!UtilString.stringValida(cronoanaliseDto.getProduto().getOid())) {
                        produtoService.montarProdutoCronoanalise(cronoanaliseDto.getProduto());
                    }
                    cronoanalise.setProduto(cronoanaliseProdutoService.montarProdutoCronoanalise(cronoanaliseDto.getProduto()));
                }
                cronoanaliseSalva = salvar(cronoanalise);
            } else {
                atualizarCronoanalise(cronoanaliseDto);
                cronoanaliseSalva.setOid(cronoanaliseDto.getOid());
            }
        }
        return cronoanaliseSalva.getOid();
    }

    public void atualizarCronoanalise(CronoanaliseDto cronoanaliseDto) {
        if (cronoanaliseDto != null) {
            if (UtilColecao.colecaoValida(cronoanaliseDto.getProduto().getOperacoes())) {
                for (CronoanaliseDto.Operacoes operacao : cronoanaliseDto.getProduto().getOperacoes()) {
                    CronoanaliseProdutoOperacao croOperacao = cronoanaliseProdutoOperacaoRepository.findByOid(operacao.getOid());
                    if (croOperacao != null) {
                        croOperacao.setObservacoes(observacaoService.montarObservacaoCronoanalise(operacao.getObservacoes()));
                        cronoanaliseProdutoOperacaoService.salvar(croOperacao);
                    }
                }
            }
        }
    }

    public CronoanaliseDto getCronoanaliseDto(String oid) {
        CronoanaliseDto dto = new CronoanaliseDto();
        Cronoanalise cronoanalise = cronoanaliseRepository.findByOid(oid);
        if (cronoanalise != null) {
            dto.setCronoanalista(cronoanalise.getCronoanalista());
            dto.setOid(cronoanalise.getOid());
            dto.setErroRelativo(cronoanalise.getErroRelativo());
            if (cronoanalise.getProduto() != null) {
                dto.setProduto(cronoanaliseProdutoService.montarProdutoCronoanaliseDto(cronoanalise.getProduto()));
            }
        }
        return dto;
    }

    public void validaCronoanalise(Cronoanalise cronoanalise) {

        if (cronoanalise == null) {
            throw new ResourceNotFoundException("Não foi possivel salvar a cronoanálise.");
        }

        if (!UtilString.stringValida(cronoanalise.getCronoanalista())) {
            throw new ResourceNotFoundException("Cronoanálista é obrigatório");
        }

        if (!UtilValor.valorValido(cronoanalise.getErroRelativo())) {
            throw new ResourceNotFoundException("Erro Relativo é obrigatório");
        }

        if (cronoanalise.getProduto() == null) {
            throw new ResourceNotFoundException("O produto é obrigatório");
        }
    }

}
