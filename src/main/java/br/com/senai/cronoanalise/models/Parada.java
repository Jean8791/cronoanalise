package br.com.senai.cronoanalise.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "parada")
public class Parada extends AbstractEntity implements Serializable {

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "tempo")
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
