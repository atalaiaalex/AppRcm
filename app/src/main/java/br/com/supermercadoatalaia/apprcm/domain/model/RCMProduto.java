package br.com.supermercadoatalaia.apprcm.domain.model;

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
public class RCMProduto {

    private Long codigo;
    private Double quantidadeUnitaria;
    private Long quantidadeEmbalagem;
    private String dataFabricacao;
    private String dataValidade;
    private Double temperatura;
    private Long horaInicio;
    private Long horaFim;
    private String lote;

    @Override
    public String toString() {
        return "RCMProduto{" +
                "codigo=" + codigo +
                ", quantidadeUnitaria=" + quantidadeUnitaria +
                ", quantidadeEmbalagem=" + quantidadeEmbalagem +
                ", dataFabricacao='" + dataFabricacao + '\'' +
                ", dataValidade='" + dataValidade + '\'' +
                ", temperatura=" + temperatura +
                ", horaInicio=" + horaInicio +
                ", horaFim=" + horaFim +
                ", lote='" + lote + '\'' +
                '}';
    }
}
