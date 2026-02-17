package br.com.senai.cronoanalise.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "tabela_coeficiente_crono_realizadas")
public class TabelaCoeficienteCronoRealizadas extends AbstractEntity implements Serializable {

    @Column(name = "observacoes")
    private Integer observacoes;

    @Column(name = "d2")
    private Double d2;

    public Integer getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(Integer observacoes) {
        this.observacoes = observacoes;
    }

    public Double getD2() {
        return d2;
    }

    public void setD2(Double d2) {
        this.d2 = d2;
    }
}
