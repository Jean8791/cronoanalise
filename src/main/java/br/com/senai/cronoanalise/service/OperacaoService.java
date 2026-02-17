package br.com.senai.cronoanalise.service;

import br.com.senai.cronoanalise.dto.CronoanaliseDto;
import br.com.senai.cronoanalise.models.Operacao;
import br.com.senai.cronoanalise.models.Produto;
import br.com.senai.cronoanalise.repository.OperacaoRepository;
import br.com.senai.cronoanalise.utils.UtilString;
import br.com.senai.cronoanalise.utils.UtilValor;
import exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OperacaoService {

    @Autowired
    private OperacaoRepository operacaoRepository;

    @Autowired
    private ObservacaoService observacaoService;

    public Operacao salvar(Operacao operacao) {
        validaOperacao(operacao);
        return operacaoRepository.save(operacao);
    }

    public Operacao atualizar(Operacao operacao) {
        validaOperacao(operacao);
        return operacaoRepository.save(operacao);
    }

    public List<Operacao> montarOperacoesCronoanalise(List<CronoanaliseDto.Operacoes> operacoes, Produto produtoSalvo) {
        List<Operacao> listOperacaoSalva = new ArrayList<>();
        for (CronoanaliseDto.Operacoes cronoOperacao : operacoes) {
            Operacao operacao = new Operacao();
            if (UtilString.stringValida(cronoOperacao.getNome())) {
                operacao.setNome(cronoOperacao.getNome());
            }
            if (UtilString.stringValida(cronoOperacao.getDescricao())) {
                operacao.setDescricao(cronoOperacao.getDescricao());
            }
            if (UtilValor.valorValido(cronoOperacao.getQuantidadeOperadores())) {
                operacao.setQuantidadeOperadores(cronoOperacao.getQuantidadeOperadores());
            }

            Operacao operacaoSalvar = salvar(operacao);

            listOperacaoSalva.add(operacaoSalvar);

        }
        return listOperacaoSalva;
    }

    private void validaOperacao(Operacao operacao) {

        if (operacao == null) {
            throw new ResourceNotFoundException("Erro ao salvar uma operacao.");
        }

        if (!UtilValor.valorValido(operacao.getQuantidadeOperadores())) {
            throw new ResourceNotFoundException("Quantidade de Operadores obrigatório.");
        }

        if (!UtilString.stringValida(operacao.getNome())) {
            throw new ResourceNotFoundException("Nome é obrigatório.");
        }
    }
}
