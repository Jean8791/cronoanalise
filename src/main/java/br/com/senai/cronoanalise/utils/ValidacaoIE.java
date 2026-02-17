package br.com.senai.cronoanalise.utils;


import org.apache.commons.lang3.BooleanUtils;

public class ValidacaoIE {
    private String ie;
    private String uf;

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    private static Boolean validaIsento(String ie) {
        Boolean retorno = ie.equals("ISENTO") ||
                ie.equals("ISENTA") ||
                ie.equals("I") ||
                ie.equals("");
        return retorno;
    }

    private static Integer converteCharToInt(String s, Integer posicao) {
        Integer retorno = 0;

        retorno = Integer.parseInt(s.substring(posicao, posicao + 1));

        return retorno;

    }

    private static Integer GetDigitoModulo(String nr, Integer base, Integer modulo) {
        if (modulo != 11 && modulo != 10) {
            modulo = 10;
        }
        Integer retorno = 0;

        nr = UtilCnpj.deixaSoNumero(nr);

        Integer b = 2;
        Integer t = 0;
        for (int i = nr.length() - 1; i >= 0; i--) {
            t = t + (b * converteCharToInt(nr, i));
            b++;
            if (b > base) {
                b = 2;
            }
        }

        retorno = t % modulo;
        retorno = modulo - retorno;
        return retorno;
    }

    private static Boolean validaAC(String ie) {
        Boolean retorno = false;

        if (ie.length() != 13) {
            return retorno;
        }
        Integer dig1 = GetDigitoModulo(ie.substring(0, 11), 9, 11);
        if (dig1 >= 10) {
            dig1 = 0;
        }
        Integer dig2 = GetDigitoModulo(ie.substring(0, 12), 9, 11);
        if (dig2 >= 10) {
            dig2 = 0;
        }

        retorno = (dig1.equals(converteCharToInt(ie, 11)) &&
                dig2.equals(converteCharToInt(ie, 12)));

        return retorno;
    }

    private static Boolean validaPadrao9digitos(String ie) {
        Boolean retorno = false;

        if (ie.length() != 9) {
            return retorno;
        }
        Integer dig = GetDigitoModulo(ie.substring(0, 8), 9, 11);
        if (dig >= 10) {
            dig = 0;
        }

        retorno = dig.equals(converteCharToInt(ie, 8));

        return retorno;
    }

    private static Boolean validaAL(String ie) {
        return validaPadrao9digitos(ie);
    }

    private static Boolean validaAP(String ie) {
        Boolean retorno = false;
        if (ie.length() != 9) {
            return retorno;
        }
        Integer b = 2;
        Integer p = 0;
        Integer d = 0;
        String ieutilizado = ie.substring(0, ie.length() - 1);
        Integer intie = Integer.parseInt(ieutilizado);


        if (intie >= 3000001 && intie <= 3017000) {
            p = 5;
            d = 0;
        } else if (intie >= 3017001 && intie <= 3019022) {
            p = 9;
            d = 1;
        }
        for (int i = ieutilizado.length() - 1; i >= 0; i--) {
            p = p + (b * converteCharToInt(ieutilizado, i));
            b++;
        }
        Integer dig = 11 - (p % 11);
        if (dig.equals(10)) {
            dig = 0;
        } else if (dig.equals(11)) {
            dig = d;
        }
        retorno = dig.equals(converteCharToInt(ie, 8));
        return retorno;
    }

    private static Boolean validaAM(String ie) {
        return validaPadrao9digitos(ie);
    }

    private static Boolean validaBA(String ie) {
        Boolean retorno;

        retorno = (ie.length() == 8 || ie.length() == 9);
        if (retorno == false) {
            return retorno;
        }
        retorno = false;

        Boolean ok = false;
        Integer dig1 = 0;
        Integer dig2 = 0;

        if (ie.length() == 9) {
            char c = ie.charAt(0);
            if (c == '0' || c == '1' || c == '2' ||
                    c == '3' || c == '4' || c == '5' ||
                    c == '8') {
                dig1 = GetDigitoModulo(ie.substring(0, 7), 8, 10);
                if (dig1 >= 10) {
                    dig1 = 0;
                }
                dig2 = GetDigitoModulo(ie.substring(0, 7) + ie.charAt(8), 9, 10);
                if (dig2 >= 10) {
                    dig2 = 0;
                }
                ok = (dig1.equals(converteCharToInt(ie, 8)) && dig2.equals(converteCharToInt(ie, 7)));
            }

            if (ok == false) {
                dig1 = GetDigitoModulo(ie.substring(0, 7), 8, 11);
                if (dig1 >= 10) {
                    dig1 = 0;
                }
                dig2 = GetDigitoModulo(ie.substring(0, 7) + ie.charAt(8), 9, 11);
                if (dig2 >= 10) {
                    dig2 = 0;
                }
            }
            retorno = (dig1.equals(converteCharToInt(ie, 8)) && dig2.equals(converteCharToInt(ie, 7)));
        } else {
            char c = ie.charAt(0);
            if (c == '0' || c == '1' || c == '2' ||
                    c == '3' || c == '4' || c == '5' ||
                    c == '8') {
                dig1 = GetDigitoModulo(ie.substring(0, 6), 9, 10);
                if (dig1 >= 10) {
                    dig1 = 0;
                }
                dig2 = GetDigitoModulo(ie.substring(0, 6) + ie.charAt(7), 9, 10);
                if (dig2 >= 10) {
                    dig2 = 0;
                }
                ok = (dig1.equals(converteCharToInt(ie, 7)) && dig2.equals(converteCharToInt(ie, 6)));

            }

            if (ok == false) {
                dig1 = GetDigitoModulo(ie.substring(0, 6), 9, 11);
                if (dig1 >= 10) {
                    dig1 = 0;
                }
                dig2 = GetDigitoModulo(ie.substring(0, 6) + ie.charAt(7), 9, 11);
                if (dig2 >= 10) {
                    dig2 = 0;
                }
            }
            retorno = (dig1.equals(converteCharToInt(ie, 7)) && dig2.equals(converteCharToInt(ie, 6)));
        }

        return retorno;
    }

    private static Boolean validaCE(String ie) {
        return validaPadrao9digitos(ie);
    }

    private static Boolean validaDF(String ie) {
        Boolean retorno = false;
        if (ie.length() != 13) {
            return retorno;
        }
        Integer dig1 = GetDigitoModulo(ie.substring(0, 11), 9, 11);
        if (dig1 >= 10) {
            dig1 = 0;
        }
        Integer dig2 = GetDigitoModulo(ie.substring(0, 12), 9, 11);
        if (dig2 >= 10) {
            dig2 = 0;
        }

        retorno = dig1.equals(converteCharToInt(ie, 11)) && dig2.equals(converteCharToInt(ie, 12));
        return retorno;
    }

    private static Boolean validaES(String ie) {
        Boolean retorno = false;

        if (ie.length() != 9) {
            return retorno;
        }

        Integer dig = GetDigitoModulo(ie.substring(0, 8), 9, 11);
        if (dig >= 10) {
            dig = 0;
        }

        retorno = dig.equals(converteCharToInt(ie, 8));

        return retorno;
    }

    private static Boolean validaGO(String ie) {
        Boolean retorno = false;

        if (ie.length() != 9) {
            return retorno;
        }

        Integer dig = GetDigitoModulo(ie.substring(0, 8), 9, 11);

        if (ie.equals("11094402")) {
            char c = ie.charAt(8);
            if (c == '0' || c == '1') {
                if (dig.equals(0) || dig.equals(1)) {
                    String s = String.valueOf(c);
                    dig = Integer.parseInt(s);
                }
            }
        } else {
            if (dig.equals(10)) {
                Integer intie = Integer.parseInt(ie.substring(0, 8));
                if (intie >= 10103105 && intie <= 10119997) {
                    dig = 1;
                } else {
                    dig = 0;
                }
            } else {
                if (dig.equals(11)) {
                    dig = 0;
                }
            }
        }

        retorno = dig.equals(converteCharToInt(ie, 8));

        return retorno;
    }

    private static Boolean validaMA(String ie) {
        Boolean retorno = false;

        if (ie.length() != 9) {
            return retorno;
        }

        retorno = ie.substring(0, 2).equals("12");
        if (retorno == false) {
            return retorno;
        }

        Integer dig = GetDigitoModulo(ie.substring(0, 8), 9, 11);
        if (dig >= 10) {
            dig = 0;
        }
        retorno = dig.equals(converteCharToInt(ie, 8));

        return retorno;

    }

    private static Boolean validaMG(String ie) {
        Boolean retorno = false;

        if (ie.substring(0, 2).equals("PR")) {
            return true;
        }
        if (ie.length() != 13) {
            return false;
        }
        String s = ie.substring(0, 11);

        s = s.substring(0, 3) + '0' + s.substring(3, s.length());
        Integer t = 0;
        Integer j = 1;
        String v = "";
        for (int i = 0; i < s.length(); i++) {
            v = String.valueOf(converteCharToInt(s, i) * j);
            for (int k = 0; k < v.length(); k++) {
                t = t + converteCharToInt(v, k);
            }
            j++;
            if (j > 2) {
                j = 1;
            }
        }
        Integer dezena = t;
        if ((t % 10) != 0) {
            Double d = Math.floor((t + 10) / 10) * 10;
            dezena = d.intValue();
        }
        Integer dig1 = dezena - t;
        Integer dig2 = GetDigitoModulo(ie.substring(0, 12), 11, 11);
        if (dig2 >= 10) {
            dig2 = 0;
        }
        retorno = (dig1.equals(converteCharToInt(ie, 11)) && dig2.equals(converteCharToInt(ie, 12)));

        return retorno;
    }

    private static Boolean validaMS(String ie) {
        Boolean retorno = false;

        if (ie.length() != 9) {
            return retorno;
        }

        Integer dig = GetDigitoModulo(ie.substring(0, 8), 9, 11);

        if (dig >= 10) {
            dig = 0;
        }

        retorno = dig.equals(converteCharToInt(ie, 8));

        return retorno;
    }

    private static Boolean validaMT(String ie) {

        Boolean retorno = false;

        if (ie.length() > 11) {
            return retorno;
        }

        while (ie.length() < 11) {
            ie = UtilString.addCaracterLeftString(ie, '0', 1);
        }

        Integer dig = GetDigitoModulo(ie.substring(0, 10), 9, 11);

        if (dig.equals(10) || dig.equals(11)) {
            dig = 0;
        }

        retorno = dig.equals(converteCharToInt(ie, 10));

        return retorno;
    }

    private static Boolean validaPA(String ie) {
        Boolean retorno = false;

        if (ie.length() != 9) {
            return retorno;
        }

        if (ie.substring(0, 2).equals("15") == false) {
            return retorno;
        }

        Integer dig = GetDigitoModulo(ie.substring(0, 8), 9, 11);
        if (dig >= 10) {
            dig = 0;
        }

        retorno = dig.equals(converteCharToInt(ie, 8));

        return retorno;
    }

    private static Boolean validaPB(String ie) {
        return validaPadrao9digitos(ie);
    }

    private static Boolean validaPE(String ie) {
        Boolean retorno = false;

        String m = "5432198765432";
        String m2 = "8765432";
        String m3 = "98765432";

        Integer dig;
        Integer d1;
        Integer d2;

        if (ie.length() == 9 || ie.length() == 14) {
            Integer t = 0;
            if (ie.length() == 14) {
                t = 0;
                for (int i = 0; i < 13; i++) {
                    t = t + (converteCharToInt(m, i) * converteCharToInt(ie, i));
                }
                dig = 11 - (t % 11);
                if (dig >= 10) {
                    dig = dig - 10;
                }
                retorno = dig.equals(converteCharToInt(ie, 13));
            } else if (ie.length() == 9) {
                t = 0;
                for (int i = 0; i < 7; i++) {
                    t = t + (converteCharToInt(m2, i) * converteCharToInt(ie, i));
                }
                dig = (t % 11);
                if (dig < 2) {
                    d1 = 0;
                } else {
                    d1 = 11 - dig;
                }
                t = 0;
                for (int i = 0; i < 8; i++) {
                    t = t + (converteCharToInt(m3, i) * converteCharToInt(ie, i));
                }
                dig = (t % 11);
                if (dig < 2) {
                    d2 = 0;
                } else {
                    d2 = 11 - dig;
                }

                retorno = d1.equals(converteCharToInt(ie, 7)) && d2.equals(converteCharToInt(ie, 8));
            }
        }

        return retorno;
    }

    private static Boolean validaPI(String ie) {
        return validaPadrao9digitos(ie);
    }

    private static Boolean validaPR(String ie) {
        Boolean retorno = false;

        if (ie.length() != 10) {
            return retorno;
        }

        Integer dig1 = GetDigitoModulo(ie.substring(0, 8), 7, 11);
        if (dig1 >= 10) {
            dig1 = 0;
        }
        Integer dig2 = GetDigitoModulo(ie.substring(0, 9), 7, 11);
        if (dig2 >= 10) {
            dig2 = 0;
        }
        retorno = dig1.equals(converteCharToInt(ie, 8)) && dig2.equals(converteCharToInt(ie, 9));

        return retorno;
    }
	/*
	function ValidaIERJ(ie: string): Boolean;
	var
	  dig: Integer;
	begin
	  Result := length(ie) = 8;
	  if not Result then
	    Exit;

	  dig := GetDigitoModulo11(copy(ie, 1, 7), 7);
	  if dig >= 10 then
	    dig := 0;

	  Result := StrToInt(ie[8]) = dig;
	end;
	*/

    private static Boolean validaRJ(String ie) {
        Boolean retorno = false;

        if (ie.length() != 8) {
            return retorno;
        }

        Integer dig = GetDigitoModulo(ie.substring(0, 7), 7, 11);
        if (dig >= 10) {
            dig = 0;
        }

        retorno = dig.equals(converteCharToInt(ie, 7));

        return retorno;
    }

    private static Boolean validaRN(String ie) {
        return validaPadrao9digitos(ie);
    }

    private static Boolean validaRO(String ie) {
        Boolean retorno = false;

        if (ie.length() != 14) {
            return retorno;
        }

        Integer dig = GetDigitoModulo(ie.substring(0, 13), 9, 11);
        if (dig >= 10) {
            dig = dig - 10;
        }

        retorno = dig.equals(converteCharToInt(ie, 13));

        return retorno;
    }

    private static Boolean validaRR(String ie) {
        Boolean retorno = false;

        if (ie.length() != 9) {
            return retorno;
        }

        if (ie.substring(0, 2).equals("24") == false) {
            return retorno;
        }

        Integer t = 0;
        for (int i = 0; i < 8; i++) {
            t = t + (converteCharToInt(ie, i) * (i + 1));
        }
        Integer dig = t % 9;

        retorno = dig.equals(converteCharToInt(ie, 8));

        return retorno;
    }

    private static Boolean validaRS(String ie) {
        Boolean retorno = false;

        if (ie.length() != 10) {
            return retorno;
        }

        Integer d = Integer.parseInt(ie.substring(0, 3));
        retorno = d >= 1 && d <= 999;
        if (retorno == false) {
            return retorno;
        }

        Integer dig = GetDigitoModulo(ie.substring(0, 9), 9, 11);
        if (dig >= 10) {
            dig = 0;
        }

        retorno = dig.equals(converteCharToInt(ie, 9));

        return retorno;
    }

    private static Boolean validaSC(String ie) {
        return validaPadrao9digitos(ie);
    }

    private static Boolean validaSE(String ie) {
        return validaPadrao9digitos(ie);
    }

    private static Boolean validaSP(String ie) {
        Boolean retorno = false;

        Integer[] p1 = new Integer[]{1, 3, 4, 5, 6, 7, 8, 10};
        Integer[] p2 = new Integer[]{3, 2, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        if (ie.length() != 12 && ie.length() != 13) {
            return retorno;
        }
        if (ie.length() == 13 && ie.charAt(0) != 'P') {
            return retorno;
        }

        Integer t;
        Integer dig1;
        Integer dig2;

        if (ie.length() == 12) {
            t = 0;
            for (int i = 0; i <= 7; i++) {
                t = t + (converteCharToInt(ie, i) * p1[i]);
            }

            dig1 = t % 11;
            if (dig1 >= 10) {
                dig1 = dig1 - 10;
            }

            t = 0;
            for (int i = 0; i <= 10; i++) {
                t = t + (converteCharToInt(ie, i) * p2[i]);
            }
            dig2 = t % 11;
            if (dig2 >= 10) {
                dig2 = dig2 - 10;
            }
            retorno = dig1.equals(converteCharToInt(ie, 8)) && dig2.equals(converteCharToInt(ie, 11));

        } else {
            t = 0;
            for (int i = 0; i <= 7; i++) {
                t = t + (converteCharToInt(ie, i) * p1[i]);
            }
            dig1 = t % 11;
            if (dig1 >= 10) {
                dig1 = dig1 - 10;
            }

            retorno = dig1.equals(converteCharToInt(ie, 9));
        }

        return retorno;
    }

    private static Boolean validaTO(String ie) {
        Boolean retorno = false;

        if (ie.length() != 9 && ie.length() != 11) {
            return retorno;
        }

        Integer dig;
        String s;

        if (ie.length() == 11) {
            s = ie.substring(2, 4);
            if (s.equals("01") || s.equals("02") || s.equals("03") || s.equals("99")) {
                s = ie.substring(0, 2) + ie.substring(4, ie.length());
                dig = GetDigitoModulo(s, 9, 11);
                if (dig >= 10) {
                    dig = 0;
                }
                retorno = dig.equals(converteCharToInt(ie, 8));
            }
        } else if (ie.length() == 9) {
            s = ie.substring(0, 8);
            dig = GetDigitoModulo(s, 9, 11);
            if (dig >= 10) {
                dig = 0;
            }
            retorno = dig.equals(converteCharToInt(ie, 8));
        }


        return retorno;
    }

    public static Boolean validaInsEst(String ie, String uf) {
        return validaInsEst(ie, uf, true);
    }

    public static Boolean validaInsEst(String ie, String uf, boolean primeiraTentativa) {
        ie = ie.trim().toUpperCase();
        String ieOriginal = ie;
        //ie = UtilCnpj.deixaSoNumero(ie);

        Boolean retorno = false;
        if (validaIsento(ieOriginal)) {
            return true;
        }

        if (uf.equals("AC")) {
            retorno = validaAC(ie);
        } else if (uf.equals("AL")) {
            retorno = validaAL(ie);
        } else if (uf.equals("AP")) {
            retorno = validaAP(ie);
        } else if (uf.equals("AM")) {
            retorno = validaAM(ie);
        } else if (uf.equals("BA")) {
            retorno = validaBA(ie);
        } else if (uf.equals("CE")) {
            retorno = validaCE(ie);
        } else if (uf.equals("DF")) {
            retorno = validaDF(ie);
        } else if (uf.equals("ES")) {
            retorno = validaES(ie);
        } else if (uf.equals("GO")) {
            retorno = validaGO(ie);
        } else if (uf.equals("MA")) {
            retorno = validaMA(ie);
        } else if (uf.equals("MT")) {
            retorno = validaMT(ie);
        } else if (uf.equals("MS")) {
            retorno = validaMS(ie);
        } else if (uf.equals("MG")) {
            retorno = validaMG(ieOriginal);
        } else if (uf.equals("PA")) {
            retorno = validaPA(ie);
        } else if (uf.equals("PB")) {
            retorno = validaPB(ie);
        } else if (uf.equals("PR")) {
            retorno = validaPR(ie);
        } else if (uf.equals("PE")) {
            retorno = validaPE(ie);
        } else if (uf.equals("PI")) {
            retorno = validaPI(ie);
        } else if (uf.equals("RJ")) {
            retorno = validaRJ(ie);
        } else if (uf.equals("RN")) {
            retorno = validaRN(ie);
        } else if (uf.equals("RS")) {
            retorno = validaRS(ie);
        } else if (uf.equals("RO")) {
            retorno = validaRO(ie);
        } else if (uf.equals("RR")) {
            retorno = validaRR(ie);
        } else if (uf.equals("SC")) {
            retorno = validaSC(ie);
        } else if (uf.equals("SP")) {
            retorno = validaSP(ie);
        } else if (uf.equals("SE")) {
            retorno = validaSE(ie);
        } else if (uf.equals("TO")) {
            retorno = validaTO(ie);
        }
        if (BooleanUtils.isNotTrue(retorno) && BooleanUtils.isTrue(primeiraTentativa)) {
            retorno = validaInsEst("0" + ie, uf, false);
        }
        return retorno;
    }

}
