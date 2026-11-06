package br.com.senai.cronoanalise.empresa;

import br.com.senai.cronoanalise.comum.EntidadeAuditada;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empresa")
public class Empresa extends EntidadeAuditada {

    @Column(nullable = false, length = 14, unique = true)
    private String cnpj;

    @Column(nullable = false, length = 160)
    private String nome;

    @Column(name = "turno_minutos", nullable = false)
    private int turnoMinutos;

    @Column(name = "fator_velocidade", nullable = false, precision = 6, scale = 3)
    private BigDecimal fatorVelocidade;

    @Column(name = "nivel_confianca", nullable = false, precision = 5, scale = 4)
    private BigDecimal nivelConfianca;

    @Column(name = "bloqueado_em")
    private Instant bloqueadoEm;

    @ElementCollection
    @CollectionTable(name = "empresa_parada", joinColumns = @JoinColumn(name = "empresa_id"))
    @OrderColumn(name = "ordem")
    private List<Parada> paradas = new ArrayList<>();

    protected Empresa() {
    }

    public Empresa(String cnpj, String nome, int turnoMinutos, BigDecimal fatorVelocidade,
                   BigDecimal nivelConfianca, List<Parada> paradas) {
        atualizar(cnpj, nome, turnoMinutos, fatorVelocidade, nivelConfianca, paradas);
    }

    public void atualizar(String cnpj, String nome, int turnoMinutos, BigDecimal fatorVelocidade,
                          BigDecimal nivelConfianca, List<Parada> paradas) {
        this.cnpj = cnpj;
        this.nome = nome;
        this.turnoMinutos = turnoMinutos;
        this.fatorVelocidade = fatorVelocidade;
        this.nivelConfianca = nivelConfianca;
        this.paradas.clear();
        this.paradas.addAll(paradas);
    }

    public boolean isBloqueada() {
        return bloqueadoEm != null;
    }

    public void bloquear() {
        if (bloqueadoEm == null) bloqueadoEm = Instant.now();
    }

    public void desbloquear() {
        bloqueadoEm = null;
    }

    public int totalParadasMinutos() {
        return paradas.stream().mapToInt(Parada::getMinutos).sum();
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getNome() {
        return nome;
    }

    public int getTurnoMinutos() {
        return turnoMinutos;
    }

    public BigDecimal getFatorVelocidade() {
        return fatorVelocidade;
    }

    public BigDecimal getNivelConfianca() {
        return nivelConfianca;
    }

    public List<Parada> getParadas() {
        return paradas;
    }
}
