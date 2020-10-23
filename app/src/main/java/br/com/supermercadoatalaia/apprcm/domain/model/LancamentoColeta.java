package br.com.supermercadoatalaia.apprcm.domain.model;

import java.io.Serializable;
import java.util.Calendar;

public class LancamentoColeta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long produtoId;
    private String produtoDescricao;
    private Double qntNaEmb;
    private Double qntEmb;
    private Double qntTotal;
    private Calendar vencimento;
    private Integer diasValidadeMinima;
    private Calendar dataAlteracao;

    public LancamentoColeta() {}

    public LancamentoColeta(Long id, Long produtoId, String produtoDescricao, Double qntNaEmb
            ,Double qntEmb, Double qntTotal, Calendar vencimento, Integer diasValidadeMinima,
                            Calendar dataAlteracao) {
        super();
        this.id = id;
        this.produtoId = produtoId;
        this.produtoDescricao = produtoDescricao;
        this.qntNaEmb = qntNaEmb;
        this.qntEmb = qntEmb;
        this.qntTotal = qntTotal;
        this.vencimento = vencimento;
        this.diasValidadeMinima = diasValidadeMinima;
        this.dataAlteracao = dataAlteracao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getProdutoDescricao() {
        return produtoDescricao;
    }

    public void setProdutoDescricao(String produtoDescricao) {
        this.produtoDescricao = produtoDescricao;
    }

    public Double getQntNaEmb() {
        return qntNaEmb;
    }

    public void setQntNaEmb(Double qntNaEmb) {
        this.qntNaEmb = qntNaEmb;
    }

    public Double getQntEmb() {
        return qntEmb;
    }

    public void setQntEmb(Double qntEmb) {
        this.qntEmb = qntEmb;
    }

    public Double getQntTotal() {
        return qntTotal;
    }

    public void setQntTotal(Double qntTotal) {
        this.qntTotal = qntTotal;
    }

    public Calendar getVencimento() {
        return vencimento;
    }

    public void setVencimento(Calendar vencimento) {
        this.vencimento = vencimento;
    }

    public Integer getDiasValidadeMinima() {
        return diasValidadeMinima;
    }

    public void setDiasValidadeMinima(Integer diasValidadeMinima) {
        this.diasValidadeMinima = diasValidadeMinima;
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
        LancamentoColeta other = (LancamentoColeta) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
