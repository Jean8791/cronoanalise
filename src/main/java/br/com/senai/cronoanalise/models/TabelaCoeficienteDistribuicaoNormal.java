package br.com.senai.cronoanalise.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "tabela_coeficiente_distribuicao_normal")
public class TabelaCoeficienteDistribuicaoNormal extends AbstractEntity implements Serializable {

    @Column(name = "coluna")
    private Double coluna;

    @Column(name = "linha")
    private Double linha;

    @Column(name = "valor")
    private Double valor;

    public Double getColuna() {
        return coluna;
    }

    public void setColuna(Double coluna) {
        this.coluna = coluna;
    }

    public Double getLinha() {
        return linha;
    }

    public void setLinha(Double linha) {
        this.linha = linha;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
