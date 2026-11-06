package br.com.senai.cronoanalise.empresa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Parada {

    @Column(nullable = false, length = 120)
    private String descricao;

    @Column(nullable = false)
    private int minutos;

    protected Parada() {
    }

    public Parada(String descricao, int minutos) {
        this.descricao = descricao;
        this.minutos = minutos;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getMinutos() {
        return minutos;
    }
}
