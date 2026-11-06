package br.com.senai.cronoanalise.calculo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalculadoraCronoanaliseTest {

    private static List<BigDecimal> tempos(double... valores) {
        return Arrays.stream(valores).mapToObj(BigDecimal::valueOf).toList();
    }

    private static EntradaCalculo exemplo() {
        return new EntradaCalculo(
                List.of(
                        new EntradaCalculo.Operacao("Cortar", tempos(10, 12, 9, 11, 8)),
                        new EntradaCalculo.Operacao("Montar", tempos(20, 19, 22, 21, 18))),
                List.of(new EntradaCalculo.Parada("Café", 10), new EntradaCalculo.Parada("Ginástica laboral", 20)),
                480,
                new BigDecimal("1.10"),
                new BigDecimal("0.95"),
                new BigDecimal("0.05"));
    }

    @Test
    void calculaMediasPorOperacaoECiclo() {
        ResultadoCalculo r = CalculadoraCronoanalise.calcular(exemplo());

        assertThat(r.observacoesRealizadas()).isEqualTo(5);
        assertThat(r.operacoes()).extracting(ResultadoCalculo.Operacao::tempoMedio)
                .containsExactly(new BigDecimal("10.00"), new BigDecimal("20.00"));
        assertThat(r.operacoes()).extracting(ResultadoCalculo.Operacao::pecasPorHora)
                .containsExactly(new BigDecimal("360.00"), new BigDecimal("180.00"));
        assertThat(r.operacoes()).extracting(ResultadoCalculo.Operacao::participacaoNoCiclo)
                .containsExactly(new BigDecimal("33.3"), new BigDecimal("66.7"));
        assertThat(r.temposCiclo()).containsExactly(
                new BigDecimal("30.00"), new BigDecimal("31.00"), new BigDecimal("31.00"),
                new BigDecimal("32.00"), new BigDecimal("26.00"));
        assertThat(r.tempoMedioCiclo()).isEqualByComparingTo("30");
        assertThat(r.amplitude()).isEqualByComparingTo("6");
    }

    @Test
    void calculaNumeroDeCiclosNecessarios() {
        ResultadoCalculo r = CalculadoraCronoanalise.calcular(exemplo());

        assertThat(r.d2()).isEqualByComparingTo("2.326");
        assertThat(r.z()).isEqualByComparingTo("1.96");
        assertThat(r.ciclosNecessarios()).isEqualByComparingTo("11.36");
        assertThat(r.ciclosNecessariosArredondado()).isEqualTo(12);
        assertThat(r.amostraSuficiente()).isFalse();
    }

    @Test
    void calculaTemposECapacidadeComOTurnoDaEmpresa() {
        ResultadoCalculo r = CalculadoraCronoanalise.calcular(exemplo());

        assertThat(r.tempoNormal()).isEqualByComparingTo("33.00");
        assertThat(r.totalParadasMinutos()).isEqualTo(30);
        assertThat(r.fatorTolerancia()).isEqualByComparingTo("1.0667");
        assertThat(r.tempoPadrao()).isEqualByComparingTo("35.20");
        assertThat(r.pecasPorHora()).isEqualByComparingTo("102.27");
        assertThat(r.pecasPorTurno()).isEqualByComparingTo("818.18");
    }

    @Test
    void turnoMaisCurtoAumentaOFatorDeTolerancia() {
        EntradaCalculo base = exemplo();
        EntradaCalculo turnoDe6h = new EntradaCalculo(base.operacoes(), base.paradas(), 360,
                base.fatorVelocidade(), base.nivelConfianca(), base.erroRelativo());

        assertThat(CalculadoraCronoanalise.calcular(turnoDe6h).fatorTolerancia()).isEqualByComparingTo("1.0909");
    }

    @Test
    void amostraGrandeOSuficienteEhMarcadaComoSuficiente() {
        EntradaCalculo e = new EntradaCalculo(
                List.of(new EntradaCalculo.Operacao("Única", tempos(10, 10.2, 9.9, 10.1, 9.8, 10))),
                List.of(), 480, BigDecimal.ONE, new BigDecimal("0.95"), new BigDecimal("0.05"));

        ResultadoCalculo r = CalculadoraCronoanalise.calcular(e);

        assertThat(r.ciclosNecessariosArredondado()).isEqualTo(1);
        assertThat(r.amostraSuficiente()).isTrue();
        assertThat(r.fatorTolerancia()).isEqualByComparingTo("1");
    }

    @Test
    void recusaOperacoesComQuantidadesDiferentesDeObservacoes() {
        EntradaCalculo e = new EntradaCalculo(
                List.of(new EntradaCalculo.Operacao("A", tempos(10, 11, 12)),
                        new EntradaCalculo.Operacao("B", tempos(10, 11))),
                List.of(), 480, BigDecimal.ONE, new BigDecimal("0.95"), new BigDecimal("0.05"));

        assertThatThrownBy(() -> CalculadoraCronoanalise.calcular(e))
                .isInstanceOf(EntradaInvalidaException.class)
                .hasMessageContaining("mesmo número de observações");
    }

    @Test
    void recusaParadasQueOcupamOTurnoInteiro() {
        EntradaCalculo e = new EntradaCalculo(
                List.of(new EntradaCalculo.Operacao("A", tempos(10, 11))),
                List.of(new EntradaCalculo.Parada("Manutenção", 480)), 480,
                BigDecimal.ONE, new BigDecimal("0.95"), new BigDecimal("0.05"));

        assertThatThrownBy(() -> CalculadoraCronoanalise.calcular(e))
                .isInstanceOf(EntradaInvalidaException.class)
                .hasMessageContaining("turno");
    }

    @Test
    void recusaTempoZeroOuNegativo() {
        EntradaCalculo e = new EntradaCalculo(
                List.of(new EntradaCalculo.Operacao("A", tempos(10, 0))),
                List.of(), 480, BigDecimal.ONE, new BigDecimal("0.95"), new BigDecimal("0.05"));

        assertThatThrownBy(() -> CalculadoraCronoanalise.calcular(e)).isInstanceOf(EntradaInvalidaException.class);
    }
}
