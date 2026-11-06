package br.com.senai.cronoanalise.calculo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class CoeficientesTest {

    @ParameterizedTest
    @CsvSource({"0.90, 1.6449", "0.95, 1.9600", "0.99, 2.5758"})
    void zBilateralDosNiveisDeConfiancaUsuais(double nivel, double esperado) {
        assertThat(Coeficientes.zBilateral(nivel)).isCloseTo(esperado, within(0.0005));
    }

    @ParameterizedTest
    @CsvSource({"2, 1.128", "5, 2.326", "10, 3.078", "25, 3.931", "27, 3.931", "30, 4.086", "100, 5.015"})
    void d2PelaQuantidadeDeObservacoes(int observacoes, String esperado) {
        assertThat(Coeficientes.d2(observacoes)).isEqualByComparingTo(esperado);
    }

    @Test
    void d2ForaDaTabelaEhRecusado() {
        assertThatThrownBy(() -> Coeficientes.d2(1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Coeficientes.d2(101)).isInstanceOf(IllegalArgumentException.class);
    }
}
