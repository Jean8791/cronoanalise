package br.com.senai.cronoanalise.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtilData {

    public static Date montaData(int ano, int mes, int dia) {
        return montaDataHora(ano, mes, dia, 0, 0, 0);
    }

    public static Date montaDataHora(int ano, int mes, int dia, int hora, int minutos, int segundos) {
        Calendar data = Calendar.getInstance();
        data.set(Calendar.YEAR, ano);
        data.set(Calendar.MONTH, mes - 1);
        data.set(Calendar.DAY_OF_MONTH, dia);

        data.set(Calendar.HOUR_OF_DAY, hora);
        data.set(Calendar.MINUTE, minutos);
        data.set(Calendar.SECOND, segundos);

        return data.getTime();
    }


    public static Date parseJsonDate(String dataString) {
        return parseJsonDate(dataString, "yyyy-MM-dd");
    }

    public static Date parseJsonDate(String dataString, String mascara) {

        if (!UtilString.stringValida(mascara)) {
            mascara = "yyyy-MM-dd";
        }

        Date data = null;
        SimpleDateFormat simple = new SimpleDateFormat(mascara);

        if (dataString != null) {
            try {
                data = simple.parse(dataString);
            } catch (ParseException e) {

            }
        }

        return data;
    }

    public static Date ultimoDiaMes(Integer mes, Integer ano) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, ano);
        calendar.set(Calendar.MONTH, mes - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static boolean dateValido(Date data) {


        if (data == null) {
            return false;
        } else {

            if (UtilData.retornaAno(data) > 1900) {
                return true;
            } else {
                return false;
            }

        }

    }

    public static DiasDaSemana diasDaSemana(Date data) {

        if (!dateValido(data)) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        if (UtilData.diaIgualDomingo(cal)) {
            return DiasDaSemana.DOMINGO;
        }

        if (UtilData.diaIgualSegunda(cal)) {
            return DiasDaSemana.SEGUNDA;
        }

        if (UtilData.diaIgualTerca(cal)) {
            return DiasDaSemana.TERCA;
        }

        if (UtilData.diaIgualQuarta(cal)) {
            return DiasDaSemana.QUARTA;
        }

        if (UtilData.diaIgualQuinta(cal)) {
            return DiasDaSemana.QUINTA;
        }
        if (UtilData.diaIgualSexta(cal)) {
            return DiasDaSemana.SEXTA;
        }

        if (UtilData.diaIgualSabado(cal)) {
            return DiasDaSemana.SABADO;
        }

        return null;
    }

    public static boolean calendarValido(Calendar data) {

        if (data == null) {
            return false;
        } else {

            return dateValido(data.getTime());

        }

    }

    public static Calendar dataAtual() {
        return Calendar.getInstance();
    }

    public static Calendar primeiroDiaMes(Calendar cal) {
        Calendar retorno = dataAtual();
        retorno.setTime(cal.getTime());
        setarDiaCalendar(retorno, 1);
        return retorno;
    }

    public static Calendar ultimoDiaMes(Calendar cal) {
        Calendar retorno = dataAtual();
        retorno.setTime(cal.getTime());
        setarDiaCalendar(retorno, 1);
        somaMesCalendar(retorno, 1);
        diminuiDiaCalendar(retorno, 1);
        return retorno;
    }

    public static Date montaData(Integer ano, Integer mes, Integer dia) {

        Calendar retorno = Calendar.getInstance();
        setarAnoCalendar(retorno, ano);
        setarMesCalendar(retorno, mes);
        setarDiaCalendar(retorno, dia);
        setarHoraCalendar(retorno, 0);
        setarMinutoCalendar(retorno, 0);
        setarSegundosCalendar(retorno, 0);

        return retorno.getTime();
    }

    public static void setarAnoCalendar(Calendar cal, int ano) {
        cal.set(Calendar.YEAR, ano);
    }

    public static void setarMesCalendar(Calendar cal, int mes) {
        cal.set(Calendar.MONTH, mes - 1);
    }

    public static void setarDiaCalendar(Calendar cal, int dia) {
        cal.set(Calendar.DAY_OF_MONTH, dia);
    }

    public static void setarHoraCalendar(Calendar cal, int hora) {
        cal.set(Calendar.HOUR_OF_DAY, hora);
    }

    public static void setarMinutosCalendar(Calendar cal, int minutos) {
        cal.set(Calendar.MINUTE, minutos);
    }

    public static void setarSegundosCalendar(Calendar cal, int segundos) {
        cal.set(Calendar.SECOND, segundos);
    }

    public static void setarMinutoCalendar(Calendar cal, int minuto) {
        cal.set(Calendar.MINUTE, minuto);
    }

    public static Date retornaSomaDiaDate(Date data) {
        return retornaSomaDiasDate(data, 1);
    }

    public static Date retornaSomaDiasDate(Date data, int dia) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        somaDiaCalendar(cal, dia);

        return cal.getTime();
    }

    public static Date retornaDiminuiDiaDate(Date data) {
        return retornaDiminuiDiasDate(data, 1);
    }

    public static Date retornaDiminuiDiasDate(Date data, int dia) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        diminuiDiaCalendar(cal, dia);

        return cal.getTime();

    }

    public static void adicionaDia(Calendar cal) {
        UtilData.somaDiaCalendar(cal, 1);
    }

    public static void somaHora(Calendar cal, Integer horas) {

        cal.add(Calendar.HOUR_OF_DAY, horas);

    }


    public static void somaDiaCalendar(Calendar cal, int qtde) {
        cal.add(Calendar.DAY_OF_MONTH, qtde);
    }

    public static void diminuiDiaCalendar(Calendar cal, int qtde) {
        cal.add(Calendar.DAY_OF_MONTH, -qtde);
    }

    public static void somaMesCalendar(Calendar cal, int qtde) {
        cal.add(Calendar.MONTH, qtde);
    }

    public static void diminuiMesCalendar(Calendar cal, int qtde) {
        cal.add(Calendar.MONTH, -qtde);
    }

    public static void somaAnoCalendar(Calendar cal, int qtde) {
        cal.add(Calendar.YEAR, qtde);
    }

    public static void diminuiAnoCalendar(Calendar cal, int qtde) {
        cal.add(Calendar.YEAR, -qtde);
    }

    public static int retornaHora(Calendar cal) {
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static int retornamMinutos(Calendar cal) {
        return cal.get(Calendar.MINUTE);
    }

    public static int retornamSegundos(Calendar cal) {
        return cal.get(Calendar.SECOND);
    }

    public static int retornaDia(Calendar cal) {
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int retornaMes(Calendar cal) {
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int retornaAno(Calendar cal) {
        return cal.get(Calendar.YEAR);
    }

    public static int retornaAno(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        return cal.get(Calendar.YEAR);
    }

    public static int retornaDia(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static String labelMes(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        return labelMes(cal.get(Calendar.MONTH) + 1);
    }

    public static String labelMes(Calendar cal) {
        return labelMes(cal.get(Calendar.MONTH) + 1);
    }

    public static String labelMes(Integer mes) {

        String label = "";

        switch (mes) {
            case 1:
                label = "Janeiro";
                break;
            case 2:
                label = "Fevereiro";
                break;
            case 3:
                label = "Março";
                break;
            case 4:
                label = "Abril";
                break;
            case 5:
                label = "Maio";
                break;
            case 6:
                label = "Junho";
                break;
            case 7:
                label = "Julho";
                break;
            case 8:
                label = "Agosto";
                break;
            case 9:
                label = "Setembro";
                break;
            case 10:
                label = "Outubro";
                break;
            case 11:
                label = "Novembro";
                break;
            case 12:
                label = "Dezembro";
                break;
        }

        return label;
    }

    public static String retornaDDMMYYYY(Calendar data) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(data.getTime());
    }

    public static String retornaDDMMYYYY(Date data) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(data.getTime());
    }

    public static String retornaDDMMYYYYFormat(Date data, String separacao) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd" + separacao + "MM" + separacao + "yyyy");
        return formatter.format(data.getTime());
    }

    public static String retornaDDMMYYYYHHMMSS(Date data) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(data.getTime());
    }

    public static String retornaDataFormat(Date data, String mascara) {
        if (dateValido(data)) {
            SimpleDateFormat formatter = new SimpleDateFormat(mascara);
            return formatter.format(data.getTime());
        }
        return null;
    }

    public static String dataHoraMaquina() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(Calendar.getInstance().getTime());
    }

    public static Date zerarHora(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    /**
     * Calcula o número de dias entre datas. Ex:
     * data_inicial = 15/10/2018
     * data_final = 16/10/2018
     * dias = 1.
     * <p>
     * Porém, caso as datas venham invertidas, ele traz o valor negativo delas. Ex:
     * data_inicial = 16/10/2018
     * data_final = 15/10/2018
     * dias = -1.
     * <p>
     * Para saber a diferença entre duas datas, utilizar o método diferencaDias, que irá a diferença real de dias.
     **/
    @Deprecated
    public static Integer calculaDias(Date data_inicial, Date data_final) {
        return diferencaDias(data_final, data_inicial);
    }

    public static Integer diferencaDias(Date data_inicial, Date data_final) {

        Integer dias = 0;

        if ((data_inicial != null) && (data_final != null)) {

            final Date dataI = DateUtils.truncate(data_inicial, Calendar.DAY_OF_MONTH);
            final Date dataF = DateUtils.truncate(data_final, Calendar.DAY_OF_MONTH);

            long calculoDias = dataF.getTime() - dataI.getTime();  // milisegundos
            calculoDias = (calculoDias / 86400000L);

            dias = Math.toIntExact(calculoDias);

        }

        return dias;
    }

    public static String formatMinutosToDDHHMM(Integer minutos) {

        if ((minutos != null) && (minutos > 0)) {

            String retorno = "";

            BigDecimal dia = (minutos > 1439) ? new BigDecimal(minutos).divide(new BigDecimal(60), RoundingMode.DOWN).divide(new BigDecimal(24), RoundingMode.DOWN) : new BigDecimal(0);

            //DIA
            retorno += UtilString.formatStringLeft(dia.toString(), '0', 2) + " - ";

            //HORA
            retorno += UtilString.formatStringLeft(Integer.valueOf(minutos / 60 % 24).toString(), '0', 2) + ":";

            //HORA
            retorno += UtilString.formatStringLeft(Integer.valueOf(minutos % 60).toString(), '0', 2);

            return retorno;
        } else {
            return "00 - 00:00";
        }


    }

    public static String retornaDDMMYYYYTHHMM(Date data) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'" + "T" + "'HH:mm");
        return formatter.format(data.getTime());
    }

    public static boolean diaMesIgualData(int pDia, int pMes, Calendar pData) {
        return pDia == retornaDia(pData) && pMes == retornaMes(pData);
    }

    public static boolean diaIgualSegunda(Calendar pData) {
        return (pData.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY);
    }

    public static boolean diaIgualTerca(Calendar pData) {
        return (pData.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY);
    }

    public static boolean diaIgualQuarta(Calendar pData) {
        return (pData.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY);
    }

    public static boolean diaIgualQuinta(Calendar pData) {
        return (pData.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY);
    }

    public static boolean diaIgualSexta(Calendar pData) {
        return (pData.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY);
    }

    public static boolean diaIgualSabado(Calendar pData) {
        return (pData.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
    }

    public static boolean diaIgualDomingo(Calendar pData) {
        return (pData.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
    }

    public static Integer difTempoMinutos(Date data_inicial, Date data_final) {

        long tempo = 0;
        if (data_inicial.getTime() > data_final.getTime()) {
            tempo = data_inicial.getTime() - data_final.getTime();
        } else {
            tempo = data_final.getTime() - data_inicial.getTime();
        }

        Double minutos = new Double(tempo / 1000 / 60);

        return minutos.intValue();

    }

    public static Calendar diaValidoLista(Calendar data, Integer[] lsDias) {
        if (lsDias != null && lsDias.length > 0) {

            for (Integer dia : lsDias) {
                if (data.get(Calendar.DATE) == Integer.valueOf(dia)) {
                    data.set(Calendar.DATE, Integer.valueOf(dia));
                    return data;
                }
            }

            Integer lastDiff = null;
            Integer diaUtilizar = null;
            for (Integer dia : lsDias) {
                Integer diff = dia - retornaDia(data);
                if (diff < 0) {
                    diff += data.getActualMaximum(Calendar.DAY_OF_MONTH);
                }
                if (lastDiff == null || diff < lastDiff) {
                    lastDiff = diff;
                    diaUtilizar = dia;
                }
            }

            if (diaUtilizar < data.get(Calendar.DATE)) {
                data.add(Calendar.MONTH, 1);
                data.set(Calendar.DATE, diaUtilizar);
            } else {
                data.set(Calendar.DATE, diaUtilizar);
            }
        }
        return data;
    }

    public static Calendar diaValidoSemana(Calendar data, Integer[] dias, Boolean segunda, Boolean terca, Boolean quarta, Boolean quinta, Boolean sexta, Boolean sabado, Boolean domingo) {

        if (segunda == null && terca == null && quarta == null && quinta == null && sexta == null && sabado == null && domingo == null) {
            return data;
        } else if (segunda != null && !segunda &&
                terca != null && !terca &&
                quarta != null && !quarta &&
                quinta != null && !quinta &&
                sexta != null && !sexta &&
                sabado != null && !sabado &&
                domingo != null && !domingo) {
            return data;
        } else {
            while (true) {
                if (diaIgualSegunda(data) && segunda != null && segunda) {
                    return data;
                } else if (diaIgualTerca(data) && terca != null && terca) {
                    return data;
                } else if (diaIgualQuarta(data) && quarta != null && quarta) {
                    return data;
                } else if (diaIgualQuinta(data) && quinta != null && quinta) {
                    return data;
                } else if (diaIgualSexta(data) && sexta != null && sexta) {
                    return data;
                } else if (diaIgualSabado(data) && sabado != null && sabado) {
                    return data;
                } else if (diaIgualDomingo(data) && domingo != null && domingo) {
                    return data;
                } else {
                    if (dias != null && dias.length > 0) {
                        for (int i = 0; i < dias.length; i++) {
                            if (dias[i] == data.get(Calendar.DATE)) {
                                Integer nextDay = (i + 1) == dias.length ? dias[0] : dias[(i + 1)];
                                if (nextDay > data.get(Calendar.DATE)) {
                                    data.set(Calendar.DATE, nextDay);
                                } else {
                                    data.add(Calendar.MONTH, 1);
                                    data.set(Calendar.DATE, nextDay);
                                }
                                break;
                            }
                        }
                    } else {
                        data.add(Calendar.DATE, 1);
                    }
                }
            }
        }
    }

    public static int retornaMes(Date data) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);

        return retornaMes(cal);
    }

    public enum DiasDaSemana {
        SEGUNDA("Segunda"),
        TERCA("Terça"),
        QUARTA("Quarta"),
        QUINTA("Quinta"),
        SEXTA("Sexta"),
        SABADO("Sábado"),
        DOMINGO("Domingo");

        private final String descricao;


        DiasDaSemana(String d) {
            descricao = d;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}
