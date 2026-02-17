package br.com.senai.cronoanalise.dto;

import java.util.List;

public class CronoanaliseDto {

    private String oid;
    private String cronoanalista;
    private Double erroRelativo;
    private String dataCriacao;
    private Produto produto;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getCronoanalista() {
        return cronoanalista;
    }

    public void setCronoanalista(String cronoanalista) {
        this.cronoanalista = cronoanalista;
    }

    public Double getErroRelativo() {
        return erroRelativo;
    }

    public void setErroRelativo(Double erroRelativo) {
        this.erroRelativo = erroRelativo;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public static class Produto {
        private String oid;
        private String nome;
        private String codigo;
        private List<Operacoes> operacoes;
        private Integer quantidadeOperacoes;
        private Integer quantidadeObservacoes;
        private Empresa empresa;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

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

        public List<Operacoes> getOperacoes() {
            return operacoes;
        }

        public void setOperacoes(List<Operacoes> operacoes) {
            this.operacoes = operacoes;
        }

        public Integer getQuantidadeOperacoes() {
            return quantidadeOperacoes;
        }

        public void setQuantidadeOperacoes(Integer quantidadeOperacoes) {
            this.quantidadeOperacoes = quantidadeOperacoes;
        }

        public Integer getQuantidadeObservacoes() {
            return quantidadeObservacoes;
        }

        public void setQuantidadeObservacoes(Integer quantidadeObservacoes) {
            this.quantidadeObservacoes = quantidadeObservacoes;
        }

        public Empresa getEmpresa() {
            return empresa;
        }

        public void setEmpresa(Empresa empresa) {
            this.empresa = empresa;
        }
    }

    public static class Operacoes {
        private String oid;
        private Integer quantidadeOperadores;
        private String nome;
        private String descricao;
        private List<Observacoes> observacoes;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public Integer getQuantidadeOperadores() {
            return quantidadeOperadores;
        }

        public void setQuantidadeOperadores(Integer quantidadeOperadores) {
            this.quantidadeOperadores = quantidadeOperadores;
        }

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

        public List<Observacoes> getObservacoes() {
            return observacoes;
        }

        public void setObservacoes(List<Observacoes> observacoes) {
            this.observacoes = observacoes;
        }
    }

    public static class Observacoes {
        private String operacao;
        private String oid;
        private Double tempo;
        private Double porcentagem;

        public String getOperacao() {
            return operacao;
        }

        public void setOperacao(String operacao) {
            this.operacao = operacao;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public Double getTempo() {
            return tempo;
        }

        public void setTempo(Double tempo) {
            this.tempo = tempo;
        }

        public Double getPorcentagem() {
            return porcentagem;
        }

        public void setPorcentagem(Double porcentagem) {
            this.porcentagem = porcentagem;
        }
    }

    public static class Empresa {
        private String oid;
        private String cnpj;
        private String nome;
        private String turno;
        private String fatorVelocidade;
        private String fatorTolerancia;
        private List<EmpresaDto.Parada> paradas;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getCnpj() {
            return cnpj;
        }

        public void setCnpj(String cnpj) {
            this.cnpj = cnpj;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getTurno() {
            return turno;
        }

        public void setTurno(String turno) {
            this.turno = turno;
        }

        public String getFatorVelocidade() {
            return fatorVelocidade;
        }

        public void setFatorVelocidade(String fatorVelocidade) {
            this.fatorVelocidade = fatorVelocidade;
        }

        public String getFatorTolerancia() {
            return fatorTolerancia;
        }

        public void setFatorTolerancia(String fatorTolerancia) {
            this.fatorTolerancia = fatorTolerancia;
        }

        public List<EmpresaDto.Parada> getParadas() {
            return paradas;
        }

        public void setParadas(List<EmpresaDto.Parada> paradas) {
            this.paradas = paradas;
        }
    }

}