package br.com.senai.cronoanalise.cronoanalise;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cronoanalise_operacao")
public class OperacaoCronometrada {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cronoanalise_id", nullable = false)
    private Cronoanalise cronoanalise;

    @Column(nullable = false)
    private int ordem;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(name = "quantidade_operadores", nullable = false)
    private int quantidadeOperadores;

    @ElementCollection
    @CollectionTable(name = "cronoanalise_observacao", joinColumns = @JoinColumn(name = "operacao_id"))
    @OrderColumn(name = "ciclo")
    @Column(name = "segundos", nullable = false, precision = 10, scale = 3)
    private List<BigDecimal> temposSegundos = new ArrayList<>();

    protected OperacaoCronometrada() {
    }

    OperacaoCronometrada(Cronoanalise cronoanalise, int ordem, String nome, int quantidadeOperadores, List<BigDecimal> tempos) {
        this.cronoanalise = cronoanalise;
        this.ordem = ordem;
        this.nome = nome;
        this.quantidadeOperadores = quantidadeOperadores;
        this.temposSegundos.addAll(tempos);
    }

    void substituirTempos(List<BigDecimal> tempos) {
        temposSegundos.clear();
        temposSegundos.addAll(tempos);
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidadeOperadores() {
        return quantidadeOperadores;
    }

    public List<BigDecimal> getTemposSegundos() {
        return temposSegundos;
    }
}
