package br.com.supermercadoatalaia.apprcm.domain.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private String flexLogin;
    private String flexSenha;
    private boolean ativo;
    private List<Long> grupos;
    private Set<String> permissoes;

    public Set<String> getGruposString() {
        Set<String> idsString = new HashSet<>();

        for(Long id : grupos) {
            idsString.add(String.valueOf(id));
        }

        return idsString;
    }
}
