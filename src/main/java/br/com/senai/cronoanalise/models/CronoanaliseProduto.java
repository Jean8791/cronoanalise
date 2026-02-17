package br.com.senai.cronoanalise.models;

import br.com.senai.cronoanalise.utils.UtilColecao;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cronoanalise_produto")
public class CronoanaliseProduto extends AbstractEntity implements Serializable {

    @Column(name = "nome")
    private String nome;

    @Column(name = "codigo")
    private String codigo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "oid_cronoanalise_produto_empresa")
    private CronoanaliseProdutoEmpresa empresa;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "oid_operacao")
    private List<CronoanaliseProdutoOperacao> operacoes;

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

    public CronoanaliseProdutoEmpresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(CronoanaliseProdutoEmpresa empresa) {
        this.empresa = empresa;
    }

    public List<CronoanaliseProdutoOperacao> getOperacoes() {
        return operacoes;
    }

    public void setOperacoes(List<CronoanaliseProdutoOperacao> operacoes) {
        if (!UtilColecao.colecaoValida(this.operacoes)) {
            this.operacoes = new ArrayList<>();
            for (CronoanaliseProdutoOperacao operacao : operacoes) {
                this.operacoes.add(operacao);
            }
        } else if (this.operacoes != operacoes) {
            this.operacoes.clear();
            if (UtilColecao.colecaoValida(operacoes)) {
                for (CronoanaliseProdutoOperacao operacao : operacoes) {
                    this.operacoes.add(operacao);
                }
            }
        }
    }
}
