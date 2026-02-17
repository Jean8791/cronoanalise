package br.com.senai.cronoanalise.models;

import br.com.senai.cronoanalise.utils.UtilColecao;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "empresa")
public class Empresa extends AbstractEntity implements Serializable {

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "nome")
    private String nome;

    @Column(name = "turno")
    private Time turno;

    @Column(name = "fator_velocidade")
    private Double fatorVelocidade;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "empresa_oid")
    private List<Parada> paradas;

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

    public Time getTurno() {
        return turno;
    }

    public void setTurno(Time turno) {
        this.turno = turno;
    }

    public Double getFatorVelocidade() {
        return fatorVelocidade;
    }

    public void setFatorVelocidade(Double fatorVelocidade) {
        this.fatorVelocidade = fatorVelocidade;
    }

    public List<Parada> getParadas() {
        return paradas;
    }

    public void setParadas(List<Parada> paradas) {
        if (!UtilColecao.colecaoValida(this.paradas)) {
            this.paradas = paradas;
        } else if (this.paradas != paradas) {
            this.paradas.clear();
            if (UtilColecao.colecaoValida(paradas)) {
                this.paradas.addAll(paradas);
            }
        }
    }
}
