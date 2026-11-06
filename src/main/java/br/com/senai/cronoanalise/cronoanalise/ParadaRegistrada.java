package br.com.senai.cronoanalise.cronoanalise;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ParadaRegistrada {

    @Column(nullable = false, length = 120)
    private String descricao;

    @Column(nullable = false)
    private int minutos;

    protected ParadaRegistrada() {
    }

    public ParadaRegistrada(String descricao, int minutos) {
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
