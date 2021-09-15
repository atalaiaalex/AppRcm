package br.com.supermercadoatalaia.apprcm.domain.model;

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
public class RCMFlex {

    private String unidade;
    private Long codigoDcto;
    private String numero;
    private String serie;
    private String dataMovimento;
    private String categoriaEntidade;
    private String codigoEntidade;
    private List<RCMProduto> produtos;
}
