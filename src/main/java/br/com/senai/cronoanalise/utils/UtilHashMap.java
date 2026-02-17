package br.com.senai.cronoanalise.utils;

import java.util.HashMap;

public class UtilHashMap {

    public static String getValueString(HashMap<String, Object> item, String campo) {
        Object v = item.get(campo);
        String retorno = null;
        if (v != null) {
            retorno = item.get(campo).toString();
        }
        return retorno;
    }


    public static Integer getValueInteger(HashMap<String, Object> item, String campo) {
        Object v = item.get(campo);
        Integer retorno = null;
        if (v != null) {
            retorno = new Integer(item.get(campo).toString());
        }
        return retorno;
    }

    public static Double getValueDouble(HashMap<String, Object> item, String campo) {
        Object v = item.get(campo);
        Double retorno = null;
        if (v != null) {
            retorno = new Double(item.get(campo).toString());
        }
        return retorno;
    }

    public static Boolean getValueBoolean(HashMap<String, Object> item, String campo) {
        Object v = item.get(campo);
        Boolean retorno = null;
        if (v != null) {
            retorno = new Boolean(item.get(campo).toString());
        } else {
            return false;
        }
        return retorno;
    }

}
