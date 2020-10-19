package br.com.supermercadoatalaia.apprcm.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Coleta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long fornecedorId;
    private Long numeroNotaFiscal;
    private Long pedidoId;
    private String unidade;
    private List<LancamentoColeta> itens = new ArrayList<>();
    private Calendar dataMovimento;
    private Calendar dataAlteracao;

    public Coleta() {}

    public Coleta(Long id, Long fornecedorId, Long numeroNotaFiscal, Long pedidoId, List<LancamentoColeta> itens,
                  Calendar dataMovimento, Calendar dataAlteracao, String unidade) {
        super();
        this.id = id;
        this.fornecedorId = fornecedorId;
        this.numeroNotaFiscal = numeroNotaFiscal;
        this.pedidoId = pedidoId;
        this.itens = itens;
        this.dataMovimento = dataMovimento;
        this.dataAlteracao = dataAlteracao;
        this.unidade = unidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public Long getNumeroNotaFiscal() {
        return numeroNotaFiscal;
    }

    public void setNumeroNotaFiscal(Long numeroNotaFiscal) {
        this.numeroNotaFiscal = numeroNotaFiscal;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public List<LancamentoColeta> getItens() {
        return itens;
    }

    public void setItens(List<LancamentoColeta> itens) {
        this.itens = itens;
    }

    public Calendar getDataMovimento() {
        return dataMovimento;
    }

    public void setDataMovimento(Calendar dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    public Calendar getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Calendar dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
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
        Coleta other = (Coleta) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
