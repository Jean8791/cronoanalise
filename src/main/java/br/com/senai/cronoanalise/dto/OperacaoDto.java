package br.com.senai.cronoanalise.dto;

public class OperacaoDto {

    private String oid;
    private String nome;
    private String descricao;
    private String quantidadeOperadores;
    private String tempo;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQuantidadeOperadores() {
        return quantidadeOperadores;
    }

    public void setQuantidadeOperadores(String quantidadeOperadores) {
        this.quantidadeOperadores = quantidadeOperadores;
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
