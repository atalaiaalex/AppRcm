package br.com.supermercadoatalaia.apprcm.domain.model;

import java.io.Serializable;

public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String transacao;
    private String status;
    private Long fornecedorId;
    private Long notaFiscalBaixada;
    private String transacaoEntrada;
    private String unidade;

    public Pedido() {}

    public Pedido(Long id, String transacao, String status, Long fornecedorId, Long notaFiscalBaixada,
                  String transacaoEntrada, String unidade) {
        super();
        this.id = id;
        this.transacao = transacao;
        this.status = status;
        this.fornecedorId = fornecedorId;
        this.notaFiscalBaixada = notaFiscalBaixada;
        this.transacaoEntrada = transacaoEntrada;
        this.unidade = unidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransacao() {
        return transacao;
    }

    public void setTransacao(String transacao) {
        this.transacao = transacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public Long getNotaFiscalBaixada() {
        return notaFiscalBaixada;
    }

    public void setNotaFiscalBaixada(Long notaFiscalBaixada) {
        this.notaFiscalBaixada = notaFiscalBaixada;
    }

    public String getTransacaoEntrada() {
        return transacaoEntrada;
    }

    public void setTransacaoEntrada(String transacaoEntrada) {
        this.transacaoEntrada = transacaoEntrada;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pedido other = (Pedido) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
