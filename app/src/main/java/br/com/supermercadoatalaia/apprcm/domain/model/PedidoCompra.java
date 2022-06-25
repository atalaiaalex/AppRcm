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
public class PedidoCompra implements Serializable {

    private Long id;
    private String transacao;
    private String status;
    private Long fornecedorId;
    private Long notaFiscalBaixada;
    private String serie;
    private String transacaoEntrada;
    private String unidade;
}
