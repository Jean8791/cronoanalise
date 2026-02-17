package br.com.senai.cronoanalise.utils;

import exception.ResourceNotFoundException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilString {

    public static String formatCnpjCpf(String texto) {
        if (texto != null) {
            if (texto.length() > 11) {
                return formatMask("##.###.###/####-##", texto);
            } else {
                return formatMask("###.###.###-##", texto);
            }
        } else {
            return texto;
        }
    }

    public static String formatMask(String mask, String texto) {
        if (mask != null && texto != null) {
            try {
                String retorno = "";
                int seqTexto = 0;
                for (int i = 0; i < mask.length(); i++) {
                    if (mask.charAt(i) == '#') {
                        retorno += texto.charAt(seqTexto);
                        seqTexto++;
                    } else {
                        retorno += mask.charAt(i);
                    }
                }
                return retorno;
            } catch (Exception e) {
                return texto;
            }
        } else {
            return texto;
        }
    }

    public static String formataTelefone(String telefone) throws Exception {
        String telefoneFinal = "";
        if (telefone != null) {
            if (telefone.length() >= 10) {
                telefoneFinal += "(";
                telefoneFinal += telefone.substring(0, 2);
                telefoneFinal += ")";
                telefoneFinal += telefone.substring(2);
            } else {
                telefoneFinal = telefone;
            }
        }
        return telefoneFinal;
    }

    public static String formataCep(String cep) throws ParseException {
        if (cep != null) {
            return cep.substring(0, 5) + "-" + cep.substring(5);
        } else {
            return null;
        }
    }

    public static boolean stringValida(String str) {
        if ((str == null) || ((str).trim().length() <= 0)) {
            return false;
        } else {
            return true;
        }
    }

    private static String[] montaArrayString(String texto, int maxChars) {
        List<String> ret = new ArrayList();
        String str = texto;
        while (str.length() > 0) {
            int posicao = str.lastIndexOf(" ", maxChars);
            if (posicao <= 0) {

                if (str.length() > maxChars) {
                    ret.add(str.substring(0, maxChars));
                    str = str.substring(maxChars, str.length());
                } else {
                    ret.add(str);
                    str = "";
                }
            } else {
                ret.add(str.substring(0, posicao));
                str = str.substring(posicao, str.length());
            }
        }
        return ret.toArray(new String[ret.size()]);
    }

    public static HashMap<String, String> formataStringObservacao(String observacao, Integer maxChars, Integer cortelinhas) throws Exception {

        String obsParte1 = "";
        String obsParte2 = "";

        if (stringValida(observacao)) {

            String str = observacao.replaceAll("\\r", "\n");

            String[] linhas = str.split("\\n");

            int qtde = 0;

            for (int i = 0; i < linhas.length; i++) {
                if (UtilString.stringValida(linhas[i])) {
                    if (qtde >= cortelinhas.intValue()) {
                        obsParte2 += linhas[i] + "\n";
                    } else {
                        String linha = linhas[i];
                        if (linha.length() > maxChars.intValue()) {
                            String[] ret = montaArrayString(linha, maxChars);
                            for (int j = 0; j < ret.length; j++) {
                                if (qtde > cortelinhas.intValue()) {
                                    obsParte2 += ret[j] + "\n";
                                } else {
                                    obsParte1 += ret[j] + "\n";
                                }
                                qtde++;
                            }
                        } else {
                            obsParte1 += linha + "\n";
                        }
                    }
                    qtde++;
                }
            }
        }

        HashMap<String, String> observacaoMap = new HashMap<>();
        observacaoMap.put(obsParte1, obsParte2);

        return observacaoMap;
    }

    public static String urlify(String string) {
        string = removerMultiplosEspacos(string);

        string = string.replaceAll("\\@", "-")
                .replaceAll("\\.", "-")
                .replaceAll("_", "-")
                .replaceAll("\\/", "-");

        Pattern pt = Pattern.compile("[^a-zA-Z0-9 -]");
        Matcher match = pt.matcher(string);
        while (match.find()) {
            String s = match.group();
            string = string.replaceAll("\\" + s, "");
        }
        string = removerEspacos(string);
        return string;
    }

    public static String removerMultiplosEspacos(String string) {
        return string.trim().replaceAll(" +", " ");
    }

    public static String removerCaracteresEspeciais(String string) {
        Pattern pt = Pattern.compile("[^a-zA-Z0-9 ]");
        Matcher match = pt.matcher(string);
        while (match.find()) {
            String s = match.group();
            string = string.replaceAll("\\" + s, "");
        }
        return string;
    }

    public static String zeroAEsquerda(String string, int size) {
        if (string == null) {
            return null;
        }
        return (String.format("%0" + String.valueOf(size) + "d", 0) + string).substring(string.length());
    }

    public static String numeroAleatorio(int size) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        char[] chars;
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                chars = "123456789".toCharArray();
            } else {
                chars = "0123456789".toCharArray();
            }
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static Date toDate(String dateStr) throws ParseException {
        if (isEmpty(dateStr)) {
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        return format.parse(dateStr);
    }

    public static String removerEspacos(String string) {
        return string.replaceAll(" ", "-").toLowerCase();
    }

    public static boolean isEmpty(String string) {
        return string == null ? true : string.trim().isEmpty();
    }

    private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean isValidCPF(String cpf) {
        if ((cpf == null) || (cpf.length() != 11)) {
            return false;
        }
        Integer digito1 = calcularDigito(cpf.substring(0, 9), pesoCPF);
        Integer digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCPF);
        return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
    }

    public static boolean isValidCNPJ(String cnpj) {
        if ((cnpj == null) || (cnpj.length() != 14)) {
            return false;
        }
        Integer digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ);
        Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ);
        return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
    }

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
            digito = Integer.parseInt(str.substring(indice, indice + 1));
            soma += digito * peso[peso.length - str.length() + indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    public static String getStringCortada(String string, int posicao) {
        if (string.length() < posicao) {
            return string;
        } else {
            return string.substring(0, posicao);
        }
    }

    public static String formatStringLeft(String text, Character caracter, int length) {
        if (text == null) return null;
        return addCaracterLeftString(text, caracter, length - text.length());
    }

    public static String formatStringRight(String text, Character caracter, int length) {
        if (text == null) return null;
        return addCaracterRightString(text, caracter, length - text.length());
    }


    public static String addCaracterLeftString(String text, Character caracter, int length) {
        if (text == null) return null;
        if (length <= 0) return text;
        for (int i = 0; i < length; i++) text = caracter + text;
        return text;
    }

    public static String addCaracterRightString(String text, Character caracter, int length) {
        if (text == null) return null;
        if (length <= 0) return text;
        for (int i = 0; i < length; i++) {
            text = text + caracter;
        }
        return text;
    }

    public static String trocaValores(String original, HashMap<String, String> hashMap) {
        if (stringValida(original) && !MapUtils.isEmpty(hashMap)) {
            Set<String> keys = hashMap.keySet();
            for (String key : keys) {
                if (StringUtils.indexOfAny(original, new String[]{key}) > -1) {
                    original = original.replace(key, hashMap.get(key));
                }
            }
        }
        return original;
    }

    public static String removeHtml(String html) {
        return Jsoup.parse(html).text();
    }

    public static Time convertStringToTime(String timeString) {
        SimpleDateFormat formatador = new SimpleDateFormat("HH:mm");
        Date data;
        try {
            data = formatador.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("Falha ao converter tempo: " + e.toString());
        }
        if (data != null) {
            return new Time(data.getTime());
        } else {
            throw new ResourceNotFoundException("Falha ao converter tempo");
        }

    }

    public static String convertTimeToString(Time time) {
        SimpleDateFormat formatador = new SimpleDateFormat("HH:mm");
        return formatador.format(time);
    }

    public static BigDecimal convertStringToBigDecimal(String string) {
       return new BigDecimal(string);
    }

    public static String convertBigDecimalToString(BigDecimal bigDecimal) {
        return bigDecimal.toString();
    }

}
