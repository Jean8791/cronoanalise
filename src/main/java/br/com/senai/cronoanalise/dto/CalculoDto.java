package br.com.senai.cronoanalise.dto;

import java.util.List;

public class CalculoDto {

    //Cáculo de Média
    private List<CalculoMedia> calculoMedia;
    private Double mediaTotal;
    private Double quantidadeTotalOperacao;
    private Double totalProducaoHora;

    //PARADAS TURNO
    private List<ParadasTurno> paradasTurnos;
    private Double totalParadas;
    private String turnoEmpresa;


    //Cálculo de tempo normal/padrão/FT
    private Double fatorVelocidade;
    private Double fatorTolerancia; // FT => formula 1 / ( 1- totalParadas)
    private Double tempoNormal; //TN -> mediaTotal * fatorVelocidade
    private Double tempoPadrao; //TP -> tempoNormal * fotorTolerancia

    //Nº CICLOS IDEAL A SER CRONOMETRADOS
    private Double x;
    private Double r; // maiorTempoObservado - menorTempoObservado
    private Double z;
    private Double d2;
    private Double er;
    private Double numeroCicloIdeal;// Math.pow((z * r) / (er * d2 * x), 2)


    //TOTAL DE PEÇAS A SER PRODUZIDAS
    private Double total1Hora; // 3600 / tempoNormal
    private Double totalTurno; // turno * total1Hora
    private Integer totalObservacoes;

    public String getTurnoEmpresa() {
        return turnoEmpresa;
    }

    public void setTurnoEmpresa(String turnoEmpresa) {
        this.turnoEmpresa = turnoEmpresa;
    }

    public List<CalculoMedia> getCalculoMedia() {
        return calculoMedia;
    }

    public void setCalculoMedia(List<CalculoMedia> calculoMedia) {
        this.calculoMedia = calculoMedia;
    }

    public Double getMediaTotal() {
        return mediaTotal;
    }

    public void setMediaTotal(Double mediaTotal) {
        this.mediaTotal = mediaTotal;
    }

    public Double getQuantidadeTotalOperacao() {
        return quantidadeTotalOperacao;
    }

    public void setQuantidadeTotalOperacao(Double quantidadeTotalOperacao) {
        this.quantidadeTotalOperacao = quantidadeTotalOperacao;
    }

    public Double getTotalProducaoHora() {
        return totalProducaoHora;
    }

    public void setTotalProducaoHora(Double totalProducaoHora) {
        this.totalProducaoHora = totalProducaoHora;
    }

    public List<ParadasTurno> getParadasTurnos() {
        return paradasTurnos;
    }

    public void setParadasTurnos(List<ParadasTurno> paradasTurnos) {
        this.paradasTurnos = paradasTurnos;
    }

    public Double getTotalParadas() {
        return totalParadas;
    }

    public void setTotalParadas(Double totalParadas) {
        this.totalParadas = totalParadas;
    }

    public Double getFatorVelocidade() {
        return fatorVelocidade;
    }

    public void setFatorVelocidade(Double fatorVelocidade) {
        this.fatorVelocidade = fatorVelocidade;
    }

    public Double getFatorTolerancia() {
        return fatorTolerancia;
    }

    public void setFatorTolerancia(Double fatorTolerancia) {
        this.fatorTolerancia = fatorTolerancia;
    }

    public Double getTempoNormal() {
        return tempoNormal;
    }

    public void setTempoNormal(Double tempoNormal) {
        this.tempoNormal = tempoNormal;
    }

    public Double getTempoPadrao() {
        return tempoPadrao;
    }

    public void setTempoPadrao(Double tempoPadrao) {
        this.tempoPadrao = tempoPadrao;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getR() {
        return r;
    }

    public void setR(Double r) {
        this.r = r;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public Double getD2() {
        return d2;
    }

    public void setD2(Double d2) {
        this.d2 = d2;
    }

    public Double getEr() {
        return er;
    }

    public void setEr(Double er) {
        this.er = er;
    }

    public Double getNumeroCicloIdeal() {
        return numeroCicloIdeal;
    }

    public void setNumeroCicloIdeal(Double numeroCicloIdeal) {
        this.numeroCicloIdeal = numeroCicloIdeal;
    }

    public Double getTotal1Hora() {
        return total1Hora;
    }

    public void setTotal1Hora(Double total1Hora) {
        this.total1Hora = total1Hora;
    }

    public Double getTotalTurno() {
        return totalTurno;
    }

    public void setTotalTurno(Double totalTurno) {
        this.totalTurno = totalTurno;
    }

    public Integer getTotalObservacoes() {
        return totalObservacoes;
    }

    public void setTotalObservacoes(Integer totalObservacoes) {
        this.totalObservacoes = totalObservacoes;
    }

    public static class CalculoMedia {
        private String oid;
        private String operacao;
        private Double media;
        private Double quantidade;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getOperacao() {
            return operacao;
        }

        public void setOperacao(String operacao) {
            this.operacao = operacao;
        }

        public Double getMedia() {
            return media;
        }

        public void setMedia(Double media) {
            this.media = media;
        }

        public Double getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Double quantidade) {
            this.quantidade = quantidade;
        }
    }

    public static class ParadasTurno {
        private String oid;
        private String descricao;
        private Integer tempo;

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public Integer getTempo() {
            return tempo;
        }

        public void setTempo(Integer tempo) {
            this.tempo = tempo;
        }
    }


}
