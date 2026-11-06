package br.com.senai.cronoanalise.produto;

import br.com.senai.cronoanalise.comum.EntidadeAuditada;
import br.com.senai.cronoanalise.empresa.Empresa;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produto")
public class Produto extends EntidadeAuditada {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(nullable = false, length = 40)
    private String codigo;

    @Column(nullable = false, length = 160)
    private String nome;

    @Column(name = "bloqueado_em")
    private Instant bloqueadoEm;

    @ElementCollection
    @CollectionTable(name = "produto_operacao", joinColumns = @JoinColumn(name = "produto_id"))
    @OrderColumn(name = "ordem")
    private List<Operacao> operacoes = new ArrayList<>();

    protected Produto() {
    }

    public Produto(Empresa empresa, String codigo, String nome, List<Operacao> operacoes) {
        atualizar(empresa, codigo, nome, operacoes);
    }

    public void atualizar(Empresa empresa, String codigo, String nome, List<Operacao> operacoes) {
        this.empresa = empresa;
        this.codigo = codigo;
        this.nome = nome;
        this.operacoes.clear();
        this.operacoes.addAll(operacoes);
    }

    public boolean isBloqueado() {
        return bloqueadoEm != null;
    }

    public void bloquear() {
        if (bloqueadoEm == null) bloqueadoEm = Instant.now();
    }

    public void desbloquear() {
        bloqueadoEm = null;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public List<Operacao> getOperacoes() {
        return operacoes;
    }
}
