package br.com.supermercadoatalaia.apprcm.dto.response;

import java.io.Serializable;
import java.util.Date;
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
public class AuthenticationToken implements Serializable {

    private String token;
    private Date expire;
    private String username;
    private Set<String> roles;
}
