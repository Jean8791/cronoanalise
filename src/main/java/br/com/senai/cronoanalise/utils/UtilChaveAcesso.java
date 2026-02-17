package br.com.senai.cronoanalise.utils;

public class UtilChaveAcesso {

    public static Boolean isCte(String chaveAcesso) {
        if (validaTamanho(chaveAcesso)) {
            if(chaveAcesso.substring(20, 22).equals("57") || chaveAcesso.substring(20, 22).equals("67")) {
                return true;
            }
        }
        return false;
    };

    public static Boolean isOnlyCte(String chaveAcesso) {
        if (validaTamanho(chaveAcesso)) {
            if(chaveAcesso.substring(20, 22).equals("57")) {
                return true;
            }
        }
        return false;
    };

    public static Boolean isCteOs(String chaveAcesso) {
        if (validaTamanho(chaveAcesso)) {
            if(chaveAcesso.substring(20, 22).equals("67")) {
                return true;
            }
        }
        return false;
    };

    public static Boolean isMDFe(String chaveAcesso) {
        if (validaTamanho(chaveAcesso)) {
            if(chaveAcesso.substring(20, 22).equals("58")) {
                return true;
            }
        }
        return false;
    };

    public static Boolean isNFe(String chaveAcesso) {
        if (validaTamanho(chaveAcesso)) {
            if(chaveAcesso.substring(20, 22).equals("55")) {
                return true;
            }
        }
        return false;
    };

    public static String getCnpj(String chaveAcesso) {
        if (validaTamanho(chaveAcesso)) {
            return chaveAcesso.substring(6,20);
        }
        return null;
    }

    public static boolean validaTamanho(String chaveAcesso) {
        return UtilString.stringValida(chaveAcesso) && new Integer(44).equals(chaveAcesso.length());
    }
}
