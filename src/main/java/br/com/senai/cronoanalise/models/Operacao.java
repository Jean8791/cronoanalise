package br.com.senai.cronoanalise.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "operacao")
public class Operacao extends AbstractEntity implements Serializable {

    @NotNull
    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @NotNull
    @Column(name = "quantidade_operadores")
    private Integer quantidadeOperadores;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantidadeOperadores() {
        return quantidadeOperadores;
    }

    public void setQuantidadeOperadores(Integer quantidadeOperadores) {
        this.quantidadeOperadores = quantidadeOperadores;
    }

}
