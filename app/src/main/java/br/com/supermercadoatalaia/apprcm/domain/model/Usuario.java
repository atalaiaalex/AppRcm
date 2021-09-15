package br.com.supermercadoatalaia.apprcm.domain.model;

public class Usuario extends UserLogin {

    private Long id;
    private String nome;

    public Usuario() {
        super();
    }

    public Usuario(Long id, String nome, String username, String password) {
        super(username, password);
        this.id = id;
        this.nome = nome;
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
}
