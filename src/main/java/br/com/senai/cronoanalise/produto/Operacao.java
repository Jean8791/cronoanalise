package br.com.senai.cronoanalise.produto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Operacao {

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(name = "quantidade_operadores", nullable = false)
    private int quantidadeOperadores;

    protected Operacao() {
    }

    public Operacao(String nome, String descricao, int quantidadeOperadores) {
        this.nome = nome;
        this.descricao = descricao;
        this.quantidadeOperadores = quantidadeOperadores;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getQuantidadeOperadores() {
        return quantidadeOperadores;
    }
}
