package br.com.senai.cronoanalise.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "cronoanalise_produto_empresa")
public class CronoanaliseProdutoEmpresa extends AbstractEntity implements Serializable {

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "nome")
    private String nome;

    @Column(name = "turno")
    private Time turno;

    @Column(name = "fator_velocidade")
    private Double fatorVelocidade;

    @OneToMany(mappedBy = "empresa")
    @Column(name = "paradas")
    private List<CronoanaliseProdutoEmpresaParada> paradas;

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

    public List<CronoanaliseProdutoEmpresaParada> getParadas() {
        return paradas;
    }

    public void setParadas(List<CronoanaliseProdutoEmpresaParada> paradas) {
        this.paradas = paradas;
    }
}
