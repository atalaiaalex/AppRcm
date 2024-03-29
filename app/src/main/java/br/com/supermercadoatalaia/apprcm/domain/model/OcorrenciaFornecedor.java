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
public class OcorrenciaFornecedor implements Serializable {

    private String mensagem;
    private Long usuarioId;
}
