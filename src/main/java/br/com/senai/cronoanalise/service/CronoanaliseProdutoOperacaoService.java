package br.com.senai.cronoanalise.service;

import br.com.senai.cronoanalise.dto.CronoanaliseDto;
import br.com.senai.cronoanalise.models.CronoanaliseProduto;
import br.com.senai.cronoanalise.models.CronoanaliseProdutoOperacao;
import br.com.senai.cronoanalise.models.Observacoes;
import br.com.senai.cronoanalise.repository.CronoanaliseProdutoOperacaoRepository;
import br.com.senai.cronoanalise.utils.UtilColecao;
import br.com.senai.cronoanalise.utils.UtilString;
import br.com.senai.cronoanalise.utils.UtilValor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CronoanaliseProdutoOperacaoService {

    @Autowired
    private ObservacaoService observacaoService;

    @Autowired
    private CronoanaliseProdutoOperacaoRepository cronoanaliseProdutoOperacaoRepository;

    public List<CronoanaliseProdutoOperacao> montarOperacoesCronoanalise(List<CronoanaliseDto.Operacoes> operacoes) {
        List<CronoanaliseProdutoOperacao> listOperacaoSalva = new ArrayList<>();
        for (CronoanaliseDto.Operacoes cronoOperacao : operacoes) {
            CronoanaliseProdutoOperacao operacao = new CronoanaliseProdutoOperacao();
            if (UtilString.stringValida(cronoOperacao.getNome())) {
                operacao.setNome(cronoOperacao.getNome());
            }
            if (UtilString.stringValida(cronoOperacao.getDescricao())) {
                operacao.setDescricao(cronoOperacao.getDescricao());
            }
            if (UtilValor.valorValido(cronoOperacao.getQuantidadeOperadores())) {
                operacao.setQuantidadeOperadores(cronoOperacao.getQuantidadeOperadores());
            }

            if (UtilColecao.colecaoValida(cronoOperacao.getObservacoes())) {
                List<Observacoes> listObsSalva = observacaoService.montarObservacaoCronoanalise(cronoOperacao.getObservacoes());
                if (UtilColecao.colecaoValida(listObsSalva)) {
                    operacao.setObservacoes(listObsSalva);
                }
            }

            CronoanaliseProdutoOperacao operacaoSalvar = salvar(operacao);

            listOperacaoSalva.add(operacaoSalvar);

            for (Observacoes observacoes : operacaoSalvar.getObservacoes()) {
                observacaoService.salvar(observacoes);
            }
        }
        return listOperacaoSalva;
    }

    public List<CronoanaliseDto.Operacoes> montarOperacoesCronoanaliseDto(List<CronoanaliseProdutoOperacao> cronoanaliseProdutoOperacaos) {
        List<CronoanaliseDto.Operacoes> operacoes = new ArrayList<>();
        for (CronoanaliseProdutoOperacao cronoanaliseProdutoOperacao : cronoanaliseProdutoOperacaos) {
            CronoanaliseDto.Operacoes operacao = new CronoanaliseDto.Operacoes();
            if (UtilString.stringValida(cronoanaliseProdutoOperacao.getOid())) {
                operacao.setOid(cronoanaliseProdutoOperacao.getOid());
            }
            if (UtilString.stringValida(cronoanaliseProdutoOperacao.getNome())) {
                operacao.setNome(cronoanaliseProdutoOperacao.getNome());
            }
            if (UtilString.stringValida(cronoanaliseProdutoOperacao.getDescricao())) {
                operacao.setDescricao(cronoanaliseProdutoOperacao.getDescricao());
            }
            if (UtilValor.valorValido(cronoanaliseProdutoOperacao.getQuantidadeOperadores())) {
                operacao.setQuantidadeOperadores(cronoanaliseProdutoOperacao.getQuantidadeOperadores());
            }
            if (UtilColecao.colecaoValida(cronoanaliseProdutoOperacao.getObservacoes())) {
                operacao.setObservacoes(observacaoService.montarObservacaoCronoanaliseDto(cronoanaliseProdutoOperacao.getObservacoes()));
            }
            operacoes.add(operacao);
        }
        return operacoes;

    }

    public CronoanaliseProdutoOperacao salvar(CronoanaliseProdutoOperacao operacao) {
        return cronoanaliseProdutoOperacaoRepository.save(operacao);
    }
}
