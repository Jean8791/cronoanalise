package br.com.senai.cronoanalise.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class UtilValor {

    public static Double formatNumeroAmericado(String pValor) throws Exception {
        NumberFormat nf = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        return nf.parse(pValor).doubleValue();
    }

    public static String formatNumeroAmericado(Double pValor, Integer decimais) {
        if (pValor == null) {
            pValor = new Double(0);
        }
        String mascara = "0";
        for (int i = 0; i < decimais; i++) {

            if (i == 0) {
                mascara += ".";
            }
            mascara += "0";
        }
        NumberFormat nf = new DecimalFormat(mascara, new DecimalFormatSymbols(Locale.US));
        return nf.format(pValor);
    }

    public static String doubleToStr(Double valor) {
        return doubleToStr(valor, 2);
    }

    public static String doubleToStr(Double valor, Integer decimais) {
        try {
            return formatNumeroAmericado(valor, decimais);
        } catch (Exception ex) {
            return "";
        }
    }

    public static Double arredondarValor(Double pValor, int pDecimais) {
        if ((pValor == null) || (pValor.isNaN()) || (pValor.isInfinite())) {
            pValor = Double.valueOf(0);
        }
        if (pValor.isInfinite()) {
            pValor = new Double("0");
        }
        if (pValor.isNaN()) {
            pValor = new Double("0");
        }
        BigDecimal retorno = new BigDecimal(pValor.toString());
        retorno = retorno.setScale(pDecimais, BigDecimal.ROUND_HALF_UP);
        return new Double(retorno.doubleValue());
    }

    public static String formatValor(Double pValor, Integer decimais) {
        if (pValor == null) {
            pValor = 0.0;
        }
        String mascara = "###,###,###,###,##0";
        for (int i = 0; i < decimais; i++) {
            if (i == 0) {
                mascara += ".";
            }
            mascara += "0";
        }
        NumberFormat nf = new DecimalFormat(mascara);
        return nf.format(pValor);
    }

    public static boolean valorMenorIgualZero(Double valor) {
        if ((valor == null) || (valor.isNaN()) || (valor.isInfinite()) || (valor.compareTo(0.00d) <= 0)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean valorMenorIgualZero(Integer valor) {
        if ((valor == null) || (valor <= 0)) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean valorValido(Double valor) {

        if ((valor != null) && (!valor.isNaN())) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean valorValido(Integer valor) {
        return valor != null;
    }

    public static boolean valorMenorIgualZeroMO(Double valor) {

        if ((valor == null) || (valor.isNaN()) || (!(valor >= 0.005))) {
            return true;
        } else {
            return false;
        }

    }


    public static String formatValor(Double pValor) throws Exception {

        NumberFormat nf = NumberFormat.getInstance();
        return nf.format(pValor);

    }

    public static Double formatarNumeroAmericano(String valor) {
        while (valor.indexOf(".") >= 0) {
            valor = valor.replace(".", "");
        }
        while (valor.indexOf(",") >= 0) {
            valor = valor.replace(",", ".");
        }
        return Double.valueOf(valor);
    }

    public static String formatNumeroEuropeu(Double pValor, Integer decimais) throws Exception {

        if (pValor == null) {
            pValor = new Double(0);
        }

        String mascara = "0";
        for (int i = 0; i < decimais; i++) {

            if (i == 0) {
                mascara += ".";
            }

            mascara += "0";
        }

        NumberFormat nf = new DecimalFormat(mascara, new DecimalFormatSymbols(Locale.GERMANY));
        return nf.format(pValor);

    }

    public static Integer retornaInteger(String valor) {

        if (valor != null) {

            String retorno = "";
            String numeros = "0123456789.,";

            try {

                for (int i = 0; i < valor.length(); i++) {
                    if (numeros.indexOf(valor.charAt(i)) >= 0) {
                        retorno += valor.charAt(i);
                    }
                }

                if (retorno.contains(",")) {
                    retorno = retorno.replace(",", ".");
                }

                if (retorno.contains(".")) {
                    retorno = retorno.substring(0, retorno.indexOf("."));
                }

                return UtilString.stringValida(retorno) ? Integer.valueOf(retorno) : null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Método para validar se o tamanho do número é valido.
     * valor = 3242 - length = 4
     */
    public static boolean validaLengthNumero(Integer valor, int length) {

        if (valorValido(valor)) {

            if (String.valueOf(BigDecimal.valueOf(valor)).length() <= length) {
                return true;
            }
        }
        return false;
    }

    /**
     * Método para validar se o tamanho do número é valido (ingnorando casas decimais).
     * valor = 120.12 - length = 3
     */
    public static boolean validaLengthNumero(Double valor, int length) {

        if (valorValido(valor)) {

            String valorStr = BigDecimal.valueOf(valor).toPlainString();

            if (valorStr.contains(".")) {
                valorStr = valorStr.substring(0, valorStr.indexOf("."));
            }

            if (valorStr.length() <= length) {
                return true;
            }
        }
        return false;
    }
}
