package br.com.senai.cronoanalise.calculo;

import java.math.BigDecimal;
import java.util.List;

public record ResultadoCalculo(
        List<Operacao> operacoes,
        List<BigDecimal> temposCiclo,
        int observacoesRealizadas,
        BigDecimal tempoMedioCiclo,
        BigDecimal amplitude,
        BigDecimal d2,
        BigDecimal z,
        BigDecimal nivelConfianca,
        BigDecimal erroRelativo,
        BigDecimal ciclosNecessarios,
        int ciclosNecessariosArredondado,
        boolean amostraSuficiente,
        BigDecimal fatorVelocidade,
        BigDecimal tempoNormal,
        List<EntradaCalculo.Parada> paradas,
        int totalParadasMinutos,
        int turnoMinutos,
        BigDecimal fatorTolerancia,
        BigDecimal tempoPadrao,
        BigDecimal pecasPorHora,
        BigDecimal pecasPorTurno
) {

    public record Operacao(
            String nome,
            BigDecimal tempoMedio,
            BigDecimal tempoMinimo,
            BigDecimal tempoMaximo,
            BigDecimal pecasPorHora,
            BigDecimal participacaoNoCiclo
    ) {
    }
}
