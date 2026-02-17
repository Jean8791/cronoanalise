package br.com.senai.cronoanalise.utils;

import java.util.Collection;
import java.util.List;

public class UtilColecao {

    public static boolean colecaoValida(Collection colecao) {
        if ((colecao==null) || (colecao.size()<=0)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean listaValida(List colecao) {
        if ((colecao==null) || (colecao.size()<=0)) {
            return false;
        } else {
            return true;
        }
    }
}
