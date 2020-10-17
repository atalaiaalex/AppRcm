package br.com.supermercadoatalaia.apprcm.domain.model;

import java.io.Serializable;

public class ProdUnidade implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String ean;
    private String descricao;
    private Double qntNaEmb;
    private String status;
    private Integer diasValidadeMinima;
    private String unidade;

    public ProdUnidade() {}

    public ProdUnidade(Long id, String ean, String descricao, Double qntNaEmb, String status,
                       Integer diasValidadeMinima, String unidade) {
        super();
        this.id = id;
        this.ean = ean;
        this.descricao = descricao;
        this.qntNaEmb = qntNaEmb;
        this.status = status;
        this.diasValidadeMinima = diasValidadeMinima;
        this.unidade = unidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getQntNaEmb() {
        return qntNaEmb;
    }

    public void setQntNaEmb(Double qntNaEmb) {
        this.qntNaEmb = qntNaEmb;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDiasValidadeMinima() {
        return diasValidadeMinima;
    }

    public void setDiasValidadeMinima(Integer diasValidadeMinima) {
        this.diasValidadeMinima = diasValidadeMinima;
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
        ProdUnidade other = (ProdUnidade) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
