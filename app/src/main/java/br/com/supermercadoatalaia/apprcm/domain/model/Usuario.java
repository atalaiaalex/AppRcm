package br.com.supermercadoatalaia.apprcm.domain.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario implements Serializable {

    private Long id;
    private String nome;
    private String username;
    private String password;
    private String flexLogin;
    private String flexSenha;
}
