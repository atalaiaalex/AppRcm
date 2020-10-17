package br.com.supermercadoatalaia.apprcm.domain.model;

import java.io.Serializable;

public class Fornecedor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String razaoSocial;
    private String cnpjCpf;
    private String situacao;
    private Long vinculoCodigo;

    public Fornecedor() {}

    public Fornecedor(Long id, String nome, String razaoSocial, String cnpjCpf, String situacao, Long vinculoCodigo) {
        super();
        this.id = id;
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.cnpjCpf = cnpjCpf;
        this.situacao = situacao;
        this.vinculoCodigo = vinculoCodigo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Long getVinculoCodigo() {
        return vinculoCodigo;
    }

    public void setVinculoCodigo(Long vinculoCodigo) {
        this.vinculoCodigo = vinculoCodigo;
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
        Fornecedor other = (Fornecedor) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
