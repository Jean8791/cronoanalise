package br.com.senai.cronoanalise.models;

import br.com.senai.cronoanalise.utils.UtilColecao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "produto")
public class Produto extends AbstractEntity implements Serializable {

    @Column(name = "nome")
    private String nome;

    @Column(name = "codigo")
    private String codigo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "oid_empresa")
    private Empresa empresa;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "produto_oid")
    private List<Operacao> operacoes;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<Operacao> getOperacoes() {
        return operacoes;
    }

    public void setOperacoes(List<Operacao> operacoes) {
        if (!UtilColecao.colecaoValida(this.operacoes)) {
            this.operacoes = new ArrayList<Operacao>();
            for (Operacao operacao : operacoes) {
                this.operacoes.add(operacao);
            }
        } else if (this.operacoes != operacoes) {
            this.operacoes.clear();
            if (UtilColecao.colecaoValida(operacoes)) {
                for (Operacao operacao : operacoes) {
                    this.operacoes.add(operacao);
                }
            }
        }
    }
}
