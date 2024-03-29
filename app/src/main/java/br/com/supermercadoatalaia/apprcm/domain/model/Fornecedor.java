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
public class Fornecedor implements Serializable {

    private Long id;
    private String nome;
    private String razaoSocial;
    private String cnpjCpf;
    private String situacao;
    private Long vinculoCodigo;
    private String status;
}
