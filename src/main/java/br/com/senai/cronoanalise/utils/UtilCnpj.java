package br.com.senai.cronoanalise.utils;

public class UtilCnpj {

    public static Boolean pessoaJuridica(String valor) {
        return valor != null && valor.trim().length() == 14;
    }

    public static String deixaSoNumero(String valor) {
        if (valor != null) {

            String retorno = "";
            String numeros = "0123456789";

            for (int i = 0; i < valor.length(); i++) {
                if (numeros.indexOf(valor.charAt(i)) >= 0) {
                    retorno += valor.charAt(i);
                }
            }

            return retorno;


        } else {
            return null;
        }

    }

    public static String formatCnpjCpf(String texto) {

        if (UtilString.stringValida(texto)) {

            if (texto.length()>11) {
                return UtilString.formatMask("##.###.###/####-##", texto);
            } else {
                return UtilString.formatMask("###.###.###-##", texto);
            }

        } else {
            return texto;
        }

    }



    public static Boolean validaCNPJCPF(String valor) {

        String msgRetorno = validaCNPJCPFmsg(valor);

        if(UtilString.stringValida(msgRetorno)){
            return false;
        }
        return true;
    }


    public static String validaCNPJCPFmsg(String valor) {

        String msgRetorno = "";

        if (deixaSoNumero(valor).length() != 11 && deixaSoNumero(valor).length() != 14) {
            msgRetorno = "CNPJ/CPF incompleto";
        } else {

            if (pessoaJuridica(deixaSoNumero(valor))){
                msgRetorno = validaCNPJ(valor);
            } else {
                msgRetorno = validaCPF(valor);
            }

        }

        return msgRetorno;
    }



    private static String validaCNPJ (String valor) {

        String valorNumero = deixaSoNumero(valor);

        int soma = 0;

        // 1º digito;
        for (int j1 = 1; j1 <= 12; j1++){


            if (j1 <5){
                soma += Integer.parseInt(valorNumero.substring(j1-1, j1)) * (6-j1);
            } else {
                soma += Integer.parseInt(valorNumero.substring(j1-1, j1)) * (14-j1);
            }
        }
        int dig1 = 11 -(soma % 11) ;
        if (dig1 > 9) {
            dig1 = 0;
        }

        // 2º digito
        soma = 0;
        for (int j2 = 1; j2 <=13; j2++){
            if (j2 < 6){
                soma += Integer.parseInt(valorNumero.substring(j2-1,j2)) * (7-j2);
            } else {
                soma += Integer.parseInt(valorNumero.substring(j2-1,j2)) * (15-j2);
            }
        }
        int dig2 = 11 - (soma % 11);
        if (dig2 > 9) {
            dig2 = 0;
        }

        // Compara dígitos
        if (dig1 != Integer.parseInt(valorNumero.substring(12,13)) || dig2 != Integer.parseInt(valorNumero.substring(13,14))){
            return "CNPJ invalido";
        } else {
            return "";
        }
    }

    private static String validaCPF (String valor) {

        String valorNumero = deixaSoNumero(valor);

        int soma = 0;

        // 1º dígito
        for (int j1 = 1; j1 <= 9; j1++){
            soma += Integer.parseInt(valorNumero.substring(9-j1, 10-j1)) * (j1+1);

        }
        int dig1 = 11 - (soma % 11);
        if(dig1 > 9){
            dig1 = 0;
        };


        // 2º dígito
        soma = 0;
        for (int j2 = 1; j2 <= 10; j2++) {
            soma += Integer.parseInt(valorNumero.substring(10-j2, 11-j2)) * (j2+1);
        }
        int dig2 = 11 - (soma % 11);
        if (dig2 > 9){
            dig2 = 0;
        }

        // compara digitos
        if (dig1 != Integer.parseInt(valorNumero.substring(9, 10)) || dig2 != Integer.parseInt(valorNumero.substring(10, 11))){
            return "CPF invalido";
        } else {
            return "";
        }

    }

    public static String getCnpjChaveAcesso(String chaveAcesso) {
        if (UtilString.stringValida(chaveAcesso) && new Integer(44).equals(chaveAcesso.length())) {
            return chaveAcesso.substring(6,20);
        }
        return null;
    }
}
