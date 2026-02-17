package br.com.senai.cronoanalise.dto;

import java.util.List;

public class ProdutoDto {

    private String oid;
    private String nome;
    private String codigo;
    private String dataCriacao;
    private EmpresaDto empresa;
    private List<OperacaoDto> operacoes;

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

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao.replace(".", "/");
    }

    public EmpresaDto getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaDto empresa) {
        this.empresa = empresa;
    }

    public List<OperacaoDto> getOperacoes() {
        return operacoes;
    }

    public void setOperacoes(List<OperacaoDto> operacoesDto) {
        this.operacoes = operacoesDto;
    }
}
