package br.com.senai.cronoanalise.cronoanalise;

import br.com.senai.cronoanalise.calculo.EntradaCalculo;
import br.com.senai.cronoanalise.comum.EntidadeAuditada;
import br.com.senai.cronoanalise.empresa.Empresa;
import br.com.senai.cronoanalise.produto.Produto;
import br.com.senai.cronoanalise.usuario.Usuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cronoanalise")
public class Cronoanalise extends EntidadeAuditada {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "criado_por", nullable = false)
    private Usuario criadoPor;

    @Column(nullable = false, length = 120)
    private String cronoanalista;

    @Column(name = "erro_relativo", nullable = false, precision = 5, scale = 4)
    private BigDecimal erroRelativo;

    @Column(name = "nivel_confianca", nullable = false, precision = 5, scale = 4)
    private BigDecimal nivelConfianca;

    @Column(name = "fator_velocidade", nullable = false, precision = 6, scale = 3)
    private BigDecimal fatorVelocidade;

    @Column(name = "turno_minutos", nullable = false)
    private int turnoMinutos;

    @Column(name = "produto_id")
    private UUID produtoId;

    @Column(name = "produto_codigo", nullable = false, length = 40)
    private String produtoCodigo;

    @Column(name = "produto_nome", nullable = false, length = 160)
    private String produtoNome;

    @Column(name = "empresa_nome", nullable = false, length = 160)
    private String empresaNome;

    @Column(name = "empresa_cnpj", nullable = false, length = 14)
    private String empresaCnpj;

    @ElementCollection
    @CollectionTable(name = "cronoanalise_parada", joinColumns = @JoinColumn(name = "cronoanalise_id"))
    @OrderColumn(name = "ordem")
    private List<ParadaRegistrada> paradas = new ArrayList<>();

    @OneToMany(mappedBy = "cronoanalise", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordem")
    private List<OperacaoCronometrada> operacoes = new ArrayList<>();

    protected Cronoanalise() {
    }

    public Cronoanalise(Usuario criadoPor, Produto produto, String cronoanalista, BigDecimal erroRelativo,
                        List<List<BigDecimal>> temposPorOperacao) {
        Empresa empresa = produto.getEmpresa();
        this.criadoPor = criadoPor;
        this.cronoanalista = cronoanalista;
        this.erroRelativo = erroRelativo;
        this.nivelConfianca = empresa.getNivelConfianca();
        this.fatorVelocidade = empresa.getFatorVelocidade();
        this.turnoMinutos = empresa.getTurnoMinutos();
        this.produtoId = produto.getId();
        this.produtoCodigo = produto.getCodigo();
        this.produtoNome = produto.getNome();
        this.empresaNome = empresa.getNome();
        this.empresaCnpj = empresa.getCnpj();
        empresa.getParadas().forEach(p -> paradas.add(new ParadaRegistrada(p.getDescricao(), p.getMinutos())));
        for (int i = 0; i < produto.getOperacoes().size(); i++) {
            var op = produto.getOperacoes().get(i);
            operacoes.add(new OperacaoCronometrada(this, i, op.getNome(), op.getQuantidadeOperadores(), temposPorOperacao.get(i)));
        }
    }

    public void corrigir(String cronoanalista, BigDecimal erroRelativo, List<List<BigDecimal>> temposPorOperacao) {
        this.cronoanalista = cronoanalista;
        this.erroRelativo = erroRelativo;
        for (int i = 0; i < operacoes.size(); i++) {
            operacoes.get(i).substituirTempos(temposPorOperacao.get(i));
        }
    }

    public EntradaCalculo paraCalculo() {
        return new EntradaCalculo(
                operacoes.stream().map(o -> new EntradaCalculo.Operacao(o.getNome(), List.copyOf(o.getTemposSegundos()))).toList(),
                paradas.stream().map(p -> new EntradaCalculo.Parada(p.getDescricao(), p.getMinutos())).toList(),
                turnoMinutos, fatorVelocidade, nivelConfianca, erroRelativo);
    }

    public int quantidadeObservacoes() {
        return operacoes.isEmpty() ? 0 : operacoes.get(0).getTemposSegundos().size();
    }

    public Usuario getCriadoPor() {
        return criadoPor;
    }

    public String getCronoanalista() {
        return cronoanalista;
    }

    public BigDecimal getErroRelativo() {
        return erroRelativo;
    }

    public BigDecimal getNivelConfianca() {
        return nivelConfianca;
    }

    public BigDecimal getFatorVelocidade() {
        return fatorVelocidade;
    }

    public int getTurnoMinutos() {
        return turnoMinutos;
    }

    public UUID getProdutoId() {
        return produtoId;
    }

    public String getProdutoCodigo() {
        return produtoCodigo;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public String getEmpresaNome() {
        return empresaNome;
    }

    public String getEmpresaCnpj() {
        return empresaCnpj;
    }

    public List<ParadaRegistrada> getParadas() {
        return paradas;
    }

    public List<OperacaoCronometrada> getOperacoes() {
        return operacoes;
    }
}
