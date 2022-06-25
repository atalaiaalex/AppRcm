package br.com.supermercadoatalaia.apprcm.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Calendar;

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
public class ProdUnidade implements Serializable {

    private Long id;
    private String ean;
    private String descricao;
    private Double qntNaEmb;
    private String status;
    private Integer diasValidadeMinima;
    private String unidade;
    private Double estoque;
    private String bloqueado;
    private String ativo;
    private Double precoVenda;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Calendar dataPrecoVenda;

    private String emOferta;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Calendar dataFimOferta;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Calendar dataUltimaCompra;

    private Double precoCompra;
    private Double qntUltCompra;
    private Double vmd;
    private Long fornecedorId;
    private Double custoCompra;
    private String setor;
}
