package br.com.senai.cronoanalise.models;

import br.com.senai.cronoanalise.utils.UtilColecao;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cronoanalise_produto_operacao")
public class CronoanaliseProdutoOperacao extends AbstractEntity implements Serializable {


    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;


    @Column(name = "quantidade_operadores")
    private Integer quantidadeOperadores;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "oid_observao")
    private List<Observacoes> observacoes;

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

    public List<Observacoes> getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(List<Observacoes> observacoes) {
        if (!UtilColecao.colecaoValida(this.observacoes)) {
            this.observacoes = new ArrayList<>();
            for (Observacoes observacao : observacoes) {
                this.observacoes.add(observacao);
            }
        } else if (this.observacoes != observacoes) {
            this.observacoes.clear();
            if (UtilColecao.colecaoValida(observacoes)) {
                for (Observacoes observacao : observacoes) {
                    this.observacoes.add(observacao);
                }
            }
        }
    }

}
