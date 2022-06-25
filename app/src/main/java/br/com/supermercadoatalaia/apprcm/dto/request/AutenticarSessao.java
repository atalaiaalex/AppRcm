package br.com.supermercadoatalaia.apprcm.dto.request;

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
public class AutenticarSessao implements Serializable {

    private String usuario;
    private String senha;
}
