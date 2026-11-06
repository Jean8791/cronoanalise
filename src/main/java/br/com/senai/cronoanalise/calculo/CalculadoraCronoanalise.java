package br.com.senai.cronoanalise.calculo;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CalculadoraCronoanalise {

    private static final MathContext MC = MathContext.DECIMAL64;
    private static final BigDecimal SEGUNDOS_POR_HORA = BigDecimal.valueOf(3600);
    private static final BigDecimal CEM = BigDecimal.valueOf(100);

    private CalculadoraCronoanalise() {
    }

    public static ResultadoCalculo calcular(EntradaCalculo entrada) {
        validar(entrada);

        int n = entrada.operacoes().get(0).temposSegundos().size();
        List<BigDecimal> temposCiclo = temposDeCiclo(entrada.operacoes(), n);
        BigDecimal tempoMedioCiclo = media(temposCiclo);

        List<ResultadoCalculo.Operacao> operacoes = new ArrayList<>();
        for (EntradaCalculo.Operacao operacao : entrada.operacoes()) {
            BigDecimal mediaOperacao = media(operacao.temposSegundos());
            operacoes.add(new ResultadoCalculo.Operacao(
                    operacao.nome(),
                    arredondar(mediaOperacao, 2),
                    arredondar(Collections.min(operacao.temposSegundos()), 2),
                    arredondar(Collections.max(operacao.temposSegundos()), 2),
                    arredondar(SEGUNDOS_POR_HORA.divide(mediaOperacao, MC), 2),
                    arredondar(mediaOperacao.multiply(CEM).divide(tempoMedioCiclo, MC), 1)));
        }

        BigDecimal amplitude = Collections.max(temposCiclo).subtract(Collections.min(temposCiclo));
        BigDecimal d2 = Coeficientes.d2(n);
        BigDecimal z = arredondar(BigDecimal.valueOf(Coeficientes.zBilateral(entrada.nivelConfianca().doubleValue())), 2);

        BigDecimal ciclosNecessarios = z.multiply(amplitude)
                .divide(entrada.erroRelativo().multiply(d2).multiply(tempoMedioCiclo), MC)
                .pow(2, MC);
        int ciclosArredondado = ciclosNecessarios.setScale(0, RoundingMode.CEILING).intValueExact();

        BigDecimal tempoNormal = tempoMedioCiclo.multiply(entrada.fatorVelocidade(), MC);

        List<EntradaCalculo.Parada> paradas = entrada.paradas() == null ? List.of() : entrada.paradas();
        int totalParadas = paradas.stream().mapToInt(EntradaCalculo.Parada::minutos).sum();
        BigDecimal fatorTolerancia = BigDecimal.valueOf(entrada.turnoMinutos())
                .divide(BigDecimal.valueOf(entrada.turnoMinutos() - totalParadas), MC);
        BigDecimal tempoPadrao = tempoNormal.multiply(fatorTolerancia, MC);

        BigDecimal pecasPorHora = SEGUNDOS_POR_HORA.divide(tempoPadrao, MC);
        BigDecimal pecasPorTurno = BigDecimal.valueOf(entrada.turnoMinutos() * 60L).divide(tempoPadrao, MC);

        return new ResultadoCalculo(
                operacoes,
                temposCiclo.stream().map(t -> arredondar(t, 2)).toList(),
                n,
                arredondar(tempoMedioCiclo, 2),
                arredondar(amplitude, 2),
                d2,
                z,
                entrada.nivelConfianca(),
                entrada.erroRelativo(),
                arredondar(ciclosNecessarios, 2),
                Math.max(ciclosArredondado, 1),
                n >= ciclosArredondado,
                entrada.fatorVelocidade(),
                arredondar(tempoNormal, 2),
                List.copyOf(paradas),
                totalParadas,
                entrada.turnoMinutos(),
                arredondar(fatorTolerancia, 4),
                arredondar(tempoPadrao, 2),
                arredondar(pecasPorHora, 2),
                arredondar(pecasPorTurno, 2));
    }

    private static List<BigDecimal> temposDeCiclo(List<EntradaCalculo.Operacao> operacoes, int n) {
        List<BigDecimal> ciclos = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            BigDecimal soma = BigDecimal.ZERO;
            for (EntradaCalculo.Operacao operacao : operacoes) {
                soma = soma.add(operacao.temposSegundos().get(i));
            }
            ciclos.add(soma);
        }
        return ciclos;
    }

    private static BigDecimal media(List<BigDecimal> valores) {
        return valores.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(valores.size()), MC);
    }

    private static BigDecimal arredondar(BigDecimal valor, int casas) {
        return valor.setScale(casas, RoundingMode.HALF_UP);
    }

    private static void validar(EntradaCalculo e) {
        if (e.operacoes() == null || e.operacoes().isEmpty()) {
            throw new EntradaInvalidaException("Informe ao menos uma operação.");
        }
        int n = e.operacoes().get(0).temposSegundos() == null ? 0 : e.operacoes().get(0).temposSegundos().size();
        if (n < Coeficientes.MIN_OBSERVACOES || n > Coeficientes.MAX_OBSERVACOES) {
            throw new EntradaInvalidaException("Cada operação precisa ter entre "
                    + Coeficientes.MIN_OBSERVACOES + " e " + Coeficientes.MAX_OBSERVACOES + " observações.");
        }
        for (EntradaCalculo.Operacao operacao : e.operacoes()) {
            if (operacao.temposSegundos() == null || operacao.temposSegundos().size() != n) {
                throw new EntradaInvalidaException("Todas as operações precisam ter o mesmo número de observações.");
            }
            if (operacao.temposSegundos().stream().anyMatch(t -> t == null || t.signum() <= 0)) {
                throw new EntradaInvalidaException("Os tempos observados devem ser maiores que zero.");
            }
        }
        if (e.fatorVelocidade() == null || e.fatorVelocidade().signum() <= 0) {
            throw new EntradaInvalidaException("O fator de velocidade deve ser maior que zero.");
        }
        if (e.nivelConfianca() == null || e.nivelConfianca().signum() <= 0 || e.nivelConfianca().compareTo(BigDecimal.ONE) >= 0) {
            throw new EntradaInvalidaException("O nível de confiança deve estar entre 0 e 1 (ex.: 0,95).");
        }
        if (e.erroRelativo() == null || e.erroRelativo().signum() <= 0 || e.erroRelativo().compareTo(BigDecimal.ONE) >= 0) {
            throw new EntradaInvalidaException("O erro relativo deve estar entre 0 e 1 (ex.: 0,05).");
        }
        int paradas = e.paradas() == null ? 0 : e.paradas().stream().mapToInt(EntradaCalculo.Parada::minutos).sum();
        if (e.turnoMinutos() <= 0 || paradas >= e.turnoMinutos()) {
            throw new EntradaInvalidaException("O turno deve ser maior que a soma das paradas.");
        }
    }
}
