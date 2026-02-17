package br.com.senai.cronoanalise.service;

import br.com.senai.cronoanalise.dto.CronoanaliseDto;
import br.com.senai.cronoanalise.models.Observacoes;
import br.com.senai.cronoanalise.repository.ObservacaoRepository;
import br.com.senai.cronoanalise.utils.UtilColecao;
import br.com.senai.cronoanalise.utils.UtilString;
import br.com.senai.cronoanalise.utils.UtilValor;
import exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ObservacaoService {

    @Autowired
    private ObservacaoRepository observacaoRepository;

    public List<Observacoes> montarObservacaoCronoanalise(List<CronoanaliseDto.Observacoes> observacoes) {
        List<Observacoes> listObsSalva = new ArrayList<>();
        for (CronoanaliseDto.Observacoes observacoesOperacao : observacoes) {
            Observacoes observacao = new Observacoes();
            if (UtilString.stringValida(observacoesOperacao.getOid())) {
                observacao.setOid(observacoesOperacao.getOid());
            }
            if (UtilValor.valorValido(observacoesOperacao.getTempo())) {
                observacao.setTempo(observacoesOperacao.getTempo());
            }
            if (UtilValor.valorValido(observacoesOperacao.getPorcentagem())) {
                observacao.setPorcentagem(observacoesOperacao.getPorcentagem());
            }
            listObsSalva.add(salvar(observacao));
        }
        return listObsSalva;
    }

    public List<CronoanaliseDto.Observacoes> montarObservacaoCronoanaliseDto(List<Observacoes> observacoes) {
        if (UtilColecao.colecaoValida(observacoes)) {
            List<CronoanaliseDto.Observacoes> observacoesList = new ArrayList<>();
            for (Observacoes obs : observacoes) {
                CronoanaliseDto.Observacoes observacao = new CronoanaliseDto.Observacoes();
                if (UtilValor.valorValido(obs.getTempo())) {
                    observacao.setTempo(obs.getTempo());
                }
                if (UtilValor.valorValido(obs.getPorcentagem())) {
                    observacao.setPorcentagem(obs.getPorcentagem());
                }
                if (UtilString.stringValida(obs.getOid())) {
                    observacao.setOid(obs.getOid());
                }
                observacoesList.add(observacao);
            }
            return observacoesList;
        }
        return null;
    }

    public Observacoes salvar(Observacoes observacao) {
        validaObservacao(observacao);
        return observacaoRepository.save(observacao);
    }

    public void validaObservacao(Observacoes observacao) {
        if (observacao == null) {
            throw new ResourceNotFoundException("Erro ao salvar uma observação.");
        }

        if (!UtilValor.valorValido(observacao.getTempo())) {
            throw new ResourceNotFoundException("Tempo da Operação é obrigatório.");
        }
    }
}
