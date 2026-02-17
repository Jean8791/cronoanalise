package br.com.senai.cronoanalise.service;

import br.com.senai.cronoanalise.dto.CalculoDto;
import br.com.senai.cronoanalise.models.*;
import br.com.senai.cronoanalise.repository.CronoanaliseProdutoEmpresaRepository;
import br.com.senai.cronoanalise.repository.TabelasCoeficienteCronoRealizadasRepository;
import br.com.senai.cronoanalise.repository.TabelasCoeficienteDistribuicaoNormalRepository;
import br.com.senai.cronoanalise.utils.UtilColecao;
import br.com.senai.cronoanalise.utils.UtilString;
import br.com.senai.cronoanalise.utils.UtilValor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class CalculoService {

    @Autowired
    private CronoanaliseProdutoEmpresaRepository cronoanaliseProdutoEmpresaRepository;

    @Autowired
    private TabelasCoeficienteDistribuicaoNormalRepository tabelasCoeficienteDistribuicaoNormalRepository;

    @Autowired
    private TabelasCoeficienteCronoRealizadasRepository tabelasCoeficienteCronoRealizadasRepository;

    public CalculoDto calcularCronoanalise(Cronoanalise cronoanalise) {
        CalculoDto calculoDto = new CalculoDto();

        if (cronoanalise.getProduto() != null) {

            if (UtilColecao.colecaoValida(cronoanalise.getProduto().getOperacoes())) {
                calculaMedia(cronoanalise.getProduto().getOperacoes(), calculoDto);

                if (cronoanalise.getProduto().getEmpresa() != null && UtilString.stringValida(cronoanalise.getProduto().getEmpresa().getOid())) {

                    CronoanaliseProdutoEmpresa empresa = cronoanaliseProdutoEmpresaRepository.findByOid(cronoanalise.getProduto().getEmpresa().getOid());

                    calculaParadas(empresa, calculoDto);

                    calculaTempoNormalPadraoFt(empresa, calculoDto);
                    calculaTotaisObservacao(calculoDto, cronoanalise.getProduto().getOperacoes());
                    calcularCicloIdeal(calculoDto, empresa, cronoanalise.getErroRelativo());
                    calculoTotalPeca(calculoDto, empresa);
                }
            }
        }


        return calculoDto;
    }

    //TOTAL DE PEÇAS A SER PRODUZIDAS
    private void calculoTotalPeca(CalculoDto calculoDto, CronoanaliseProdutoEmpresa empresa) {
        calculoDto.setTurnoEmpresa(new SimpleDateFormat("HH:mm").format(empresa.getTurno()));
        Double total1Hora = UtilValor.arredondarValor(3600 / calculoDto.getTempoPadrao(), 2);
        Double turno = 0d;
        if (empresa.getTurno() != null) {
            if (!UtilValor.valorMenorIgualZero(empresa.getTurno().getHours())) {
                turno += empresa.getTurno().getHours();
            }
            if (!UtilValor.valorMenorIgualZero(empresa.getTurno().getMinutes())) {
                turno += empresa.getTurno().getMinutes() / 60d;
            }
        }
        Double total8Horas = (turno * total1Hora);
        calculoDto.setTotal1Hora(UtilValor.arredondarValor(total1Hora, 2));
        calculoDto.setTotalTurno(UtilValor.arredondarValor(total8Horas, 2));
    }

    //CICLOS IDEAL A SER CRONOMETRADOS
    public void calcularCicloIdeal(CalculoDto calculoDto, CronoanaliseProdutoEmpresa empresa, Double erroRelativo) {
        Double x = calculoDto.getMediaTotal();
        Double r = calculoDto.getR();
        Double z = 0d;

        if (!UtilValor.valorMenorIgualZero(empresa.getFatorVelocidade())) {
            List<TabelaCoeficienteDistribuicaoNormal> tabelaCoeficienteDistribuicaoNormal = tabelasCoeficienteDistribuicaoNormalRepository.findAll();
            Double fatorVelocidade = empresa.getFatorVelocidade();

            Double min = Double.MAX_VALUE;

            for (TabelaCoeficienteDistribuicaoNormal tabela : tabelaCoeficienteDistribuicaoNormal) {
                final Double diff = Math.abs(tabela.getValor() - fatorVelocidade);

                if (diff < min) {
                    min = diff;
                    z = UtilValor.arredondarValor(tabela.getColuna() + tabela.getLinha(), 2);
                }
            }
        }

        Double d2 = 0d;

        if (!UtilValor.valorMenorIgualZero(calculoDto.getTotalObservacoes())) {
            List<TabelaCoeficienteCronoRealizadas> tabelaCoeficienteCronoRealizadas = tabelasCoeficienteCronoRealizadasRepository.findAll();
            for (TabelaCoeficienteCronoRealizadas tabela : tabelaCoeficienteCronoRealizadas) {
                if (tabela.getObservacoes().equals(calculoDto.getTotalObservacoes())) {
                    d2 = tabela.getD2();
                }
            }
        }

        Double er = erroRelativo;

        Double numeroCiclos = Math.pow((z * r) / (er * d2 * x), 2);

        calculoDto.setX(x);
        calculoDto.setZ(z);
        calculoDto.setD2(d2);
        calculoDto.setEr(er);
        calculoDto.setNumeroCicloIdeal(UtilValor.arredondarValor(numeroCiclos, 2));
    }

    //Cálculo de tempo normal/padrão/FT
    public void calculaTempoNormalPadraoFt(CronoanaliseProdutoEmpresa empresa, CalculoDto calculoDto) {
        if (UtilValor.valorValido(empresa.getFatorVelocidade())) {

            Double fatorTolerancia = 480 / (480 - calculoDto.getTotalParadas());
            Double fatorVelocidade = empresa.getFatorVelocidade();
            Double tempoNormal = calculoDto.getMediaTotal() * fatorVelocidade;
            Double tempoPadrao = tempoNormal * fatorTolerancia;

            calculoDto.setFatorVelocidade(fatorVelocidade);
            calculoDto.setFatorTolerancia(UtilValor.arredondarValor(fatorTolerancia, 2));
            calculoDto.setTempoNormal(UtilValor.arredondarValor(tempoNormal, 2));
            calculoDto.setTempoPadrao(UtilValor.arredondarValor(tempoPadrao, 2));
        }
    }

    private void calculaMedia(List<CronoanaliseProdutoOperacao> operacoes, CalculoDto calculoDto) {
        List<CalculoDto.CalculoMedia> calculoMedias = new ArrayList<>();
        Double mediaTotal = 0.0;
        Double quantidadeTotalOperacao = 0.0;
        Double totalProducaoHora = 0.0;
        int count = 0;

        for (CronoanaliseProdutoOperacao operacao : operacoes) {

            if (UtilColecao.colecaoValida(operacao.getObservacoes())) {

                Double totalObservacao = 0.0;

                CalculoDto.CalculoMedia calculoMedia = new CalculoDto.CalculoMedia();

                for (Observacoes observacoes : operacao.getObservacoes()) {
                    totalObservacao += observacoes.getTempo();
                }

                Double media = totalObservacao / operacao.getObservacoes().size();

                Double quantidade = 3600 / media;

                calculoMedia.setMedia(UtilValor.arredondarValor(media, 2));

                mediaTotal += media;
                quantidadeTotalOperacao += quantidade;
                calculoMedia.setQuantidade(UtilValor.arredondarValor(quantidade, 2));
                calculoMedia.setOperacao(operacao.getNome());
                calculoMedias.add(calculoMedia);

            }
            count++;
        }

        totalProducaoHora = quantidadeTotalOperacao / operacoes.size();
        calculoDto.setCalculoMedia(calculoMedias);
        calculoDto.setMediaTotal(UtilValor.arredondarValor(mediaTotal, 2));
        calculoDto.setQuantidadeTotalOperacao(UtilValor.arredondarValor(quantidadeTotalOperacao, 2));
        calculoDto.setTotalProducaoHora(UtilValor.arredondarValor(totalProducaoHora, 2));
        calculoDto.setTotalObservacoes(operacoes.get(0).getObservacoes().size());
    }

    public void calculaParadas(CronoanaliseProdutoEmpresa empresa, CalculoDto calculoDto) {
        Double total = 0.0;
        List<CalculoDto.ParadasTurno> paradasTurnos = new ArrayList<>();
        for (CronoanaliseProdutoEmpresaParada parada : empresa.getParadas()) {
            CalculoDto.ParadasTurno paradasTurno = new CalculoDto.ParadasTurno();
            paradasTurno.setDescricao(parada.getDescricao());
            paradasTurno.setTempo(parada.getTempo());
            total += parada.getTempo();
            paradasTurnos.add(paradasTurno);
        }
        calculoDto.setParadasTurnos(paradasTurnos);
        calculoDto.setTotalParadas(total);
    }

    public void calculaTotaisObservacao(CalculoDto calculoDto, List<CronoanaliseProdutoOperacao> operacoes) {
        List<Double> listMedias = new ArrayList<>();
        for (int i = 0; i < operacoes.get(0).getObservacoes().size(); i++) {
            Double valor = 0.0;
            for (int c = 0; c < operacoes.size(); c++) {
                valor += operacoes.get(c).getObservacoes().get(i).getTempo();
            }
            listMedias.add(valor);
        }
        if (UtilColecao.colecaoValida(listMedias)) {
            DoubleSummaryStatistics summary = listMedias.stream().collect(Collectors.summarizingDouble(Double::doubleValue));
            calculoDto.setR(UtilValor.arredondarValor(summary.getMax() - summary.getMin(), 2));
        }
    }
}
