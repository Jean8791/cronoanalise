package br.com.senai.cronoanalise.calculo;

import java.math.BigDecimal;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class Coeficientes {

    public static final int MIN_OBSERVACOES = 2;
    public static final int MAX_OBSERVACOES = 100;

    private static final NavigableMap<Integer, BigDecimal> D2 = new TreeMap<>(Map.ofEntries(
            Map.entry(2, new BigDecimal("1.128")), Map.entry(3, new BigDecimal("1.693")),
            Map.entry(4, new BigDecimal("2.059")), Map.entry(5, new BigDecimal("2.326")),
            Map.entry(6, new BigDecimal("2.534")), Map.entry(7, new BigDecimal("2.704")),
            Map.entry(8, new BigDecimal("2.847")), Map.entry(9, new BigDecimal("2.970")),
            Map.entry(10, new BigDecimal("3.078")), Map.entry(11, new BigDecimal("3.173")),
            Map.entry(12, new BigDecimal("3.258")), Map.entry(13, new BigDecimal("3.336")),
            Map.entry(14, new BigDecimal("3.407")), Map.entry(15, new BigDecimal("3.472")),
            Map.entry(16, new BigDecimal("3.532")), Map.entry(17, new BigDecimal("3.588")),
            Map.entry(18, new BigDecimal("3.640")), Map.entry(19, new BigDecimal("3.689")),
            Map.entry(20, new BigDecimal("3.735")), Map.entry(21, new BigDecimal("3.778")),
            Map.entry(22, new BigDecimal("3.819")), Map.entry(23, new BigDecimal("3.858")),
            Map.entry(24, new BigDecimal("3.895")), Map.entry(25, new BigDecimal("3.931")),
            Map.entry(30, new BigDecimal("4.086")), Map.entry(35, new BigDecimal("4.213")),
            Map.entry(40, new BigDecimal("4.322")), Map.entry(45, new BigDecimal("4.415")),
            Map.entry(50, new BigDecimal("4.498")), Map.entry(55, new BigDecimal("4.572")),
            Map.entry(60, new BigDecimal("4.609")), Map.entry(65, new BigDecimal("4.639")),
            Map.entry(70, new BigDecimal("4.755")), Map.entry(75, new BigDecimal("4.806")),
            Map.entry(80, new BigDecimal("4.854")), Map.entry(85, new BigDecimal("4.898")),
            Map.entry(90, new BigDecimal("4.939")), Map.entry(95, new BigDecimal("4.978")),
            Map.entry(100, new BigDecimal("5.015"))));

    private Coeficientes() {
    }

    public static BigDecimal d2(int observacoes) {
        if (observacoes < MIN_OBSERVACOES || observacoes > MAX_OBSERVACOES) {
            throw new IllegalArgumentException("Número de observações fora da tabela d2: " + observacoes);
        }
        return D2.floorEntry(observacoes).getValue();
    }

    public static double zBilateral(double nivelConfianca) {
        if (nivelConfianca <= 0 || nivelConfianca >= 1) {
            throw new IllegalArgumentException("Nível de confiança deve estar entre 0 e 1.");
        }
        return inversaNormalPadrao((1 + nivelConfianca) / 2);
    }

    static double inversaNormalPadrao(double p) {
        final double[] a = {-3.969683028665376e+01, 2.209460984245205e+02, -2.759285104469687e+02,
                1.383577518672690e+02, -3.066479806614716e+01, 2.506628277459239e+00};
        final double[] b = {-5.447609879822406e+01, 1.615858368580409e+02, -1.556989798598866e+02,
                6.680131188771972e+01, -1.328068155288572e+01};
        final double[] c = {-7.784894002430293e-03, -3.223964580411365e-01, -2.400758277161838e+00,
                -2.549732539343734e+00, 4.374664141464968e+00, 2.938163982698783e+00};
        final double[] d = {7.784695709041462e-03, 3.224671290700398e-01, 2.445134137142996e+00,
                3.754408661907416e+00};
        final double baixo = 0.02425;
        final double alto = 1 - baixo;

        if (p < baixo) {
            double q = Math.sqrt(-2 * Math.log(p));
            return (((((c[0] * q + c[1]) * q + c[2]) * q + c[3]) * q + c[4]) * q + c[5])
                    / ((((d[0] * q + d[1]) * q + d[2]) * q + d[3]) * q + 1);
        }
        if (p > alto) {
            double q = Math.sqrt(-2 * Math.log(1 - p));
            return -(((((c[0] * q + c[1]) * q + c[2]) * q + c[3]) * q + c[4]) * q + c[5])
                    / ((((d[0] * q + d[1]) * q + d[2]) * q + d[3]) * q + 1);
        }
        double q = p - 0.5;
        double r = q * q;
        return (((((a[0] * r + a[1]) * r + a[2]) * r + a[3]) * r + a[4]) * r + a[5]) * q
                / (((((b[0] * r + b[1]) * r + b[2]) * r + b[3]) * r + b[4]) * r + 1);
    }
}
