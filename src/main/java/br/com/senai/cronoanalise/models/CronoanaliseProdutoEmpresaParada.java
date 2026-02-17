package br.com.senai.cronoanalise.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cronoanalise_produto_empresa_parada")
public class CronoanaliseProdutoEmpresaParada extends AbstractEntity implements Serializable {

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "tempo")
    private Integer tempo;

    @ManyToOne
    @JoinColumn(name = "oid_cronoanalise_produto_empresa")
    private CronoanaliseProdutoEmpresa empresa;

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

    public CronoanaliseProdutoEmpresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(CronoanaliseProdutoEmpresa empresa) {
        this.empresa = empresa;
    }
}
