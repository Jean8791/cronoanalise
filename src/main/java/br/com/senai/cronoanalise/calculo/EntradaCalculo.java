package br.com.senai.cronoanalise.calculo;

import java.math.BigDecimal;
import java.util.List;

public record EntradaCalculo(
        List<Operacao> operacoes,
        List<Parada> paradas,
        int turnoMinutos,
        BigDecimal fatorVelocidade,
        BigDecimal nivelConfianca,
        BigDecimal erroRelativo
) {

    public record Operacao(String nome, List<BigDecimal> temposSegundos) {
    }

    public record Parada(String descricao, int minutos) {
    }
}
