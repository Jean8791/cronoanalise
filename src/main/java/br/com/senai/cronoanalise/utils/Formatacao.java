package br.com.senai.cronoanalise.utils;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public class Formatacao {

    public static final String CEP = "#####-###";
    public static final String CPF = "###.###.###-##";
    public static final String CNPJ = "##.###.###/####-##";
    public static final String TELEFONE = "telefone";
    public static final String TELEFONE8 = "(##) #### - ####";
    public static final String TELEFONE9 = "(##) ##### - ####";

    public static String formatar(String pattern, Object value) {

        if (value == null){
            return "";
        }

        if (pattern == "telefone"){
            if (value.toString().length() == 11){
                pattern = TELEFONE9;
            } else {
                pattern = TELEFONE8;
            }
        }

        MaskFormatter mask;

        try {
            mask = new MaskFormatter(pattern);
            mask.setValueContainsLiteralCharacters(false);
            return mask.valueToString(value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
