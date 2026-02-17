package br.com.senai.cronoanalise.dto;

import java.util.List;

public class EmpresaDto {

    private String oid;
    private String cnpj;
    private String nome;
    private String turno;
    private String fatorVelocidade;
    private List<Parada> paradas;
    private String dataCriacao;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getFatorVelocidade() {
        return fatorVelocidade;
    }

    public void setFatorVelocidade(String fatorVelocidade) {
        this.fatorVelocidade = fatorVelocidade;
    }

    public List<Parada> getParadas() {
        return paradas;
    }

    public void setParadas(List<Parada> paradas) {
        this.paradas = paradas;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao.replace(".", "/");
    }

    public static class Parada {
        private String oid;
        private String descricao;
        private String tempo;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public String getTempo() {
            return tempo;
        }

        public void setTempo(String tempo) {
            this.tempo = tempo;
        }
    }
}
