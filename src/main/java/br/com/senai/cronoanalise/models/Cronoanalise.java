package br.com.senai.cronoanalise.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cronoanalise")
public class Cronoanalise extends AbstractEntity implements Serializable {

    @Column(name = "cronoanalista")
    private String cronoanalista;

    @Column(name = "erro_relativo")
    private Double erroRelativo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "oid_produto")
    private CronoanaliseProduto produto;

    public String getCronoanalista() {
        return cronoanalista;
    }

    public void setCronoanalista(String cronoanalista) {
        this.cronoanalista = cronoanalista;
    }

    public Double getErroRelativo() {
        return erroRelativo;
    }

    public void setErroRelativo(Double erroRelativo) {
        this.erroRelativo = erroRelativo;
    }

    public CronoanaliseProduto getProduto() {
        return produto;
    }

    public void setProduto(CronoanaliseProduto produto) {
        this.produto = produto;
    }
}
