package br.com.supermercadoatalaia.apprcm.domain.model;

import java.io.Serializable;
import java.util.List;

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
public class RespostaAutenticacao implements Serializable {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response implements Serializable {

        private String status;
        private List<Message> messages;
        private String token;
        private String tokenExpiration;
    }

    private Response response;
}
