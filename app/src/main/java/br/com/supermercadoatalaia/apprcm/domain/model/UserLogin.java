package br.com.supermercadoatalaia.apprcm.domain.model;

import java.io.Serializable;

public class UserLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public UserLogin() {}

    public UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
