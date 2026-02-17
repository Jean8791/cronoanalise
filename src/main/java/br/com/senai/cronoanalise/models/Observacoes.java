package br.com.senai.cronoanalise.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "observacao")
public class Observacoes extends AbstractEntity implements Serializable {

    @Column(name = "tempo")
    private Double tempo;

    @Column(name = "porcentagem")
    private Double porcentagem;

    public Double getTempo() {
        return tempo;
    }

    public void setTempo(Double tempo) {
        this.tempo = tempo;
    }

    public Double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(Double porcentagem) {
        this.porcentagem = porcentagem;
    }

}
