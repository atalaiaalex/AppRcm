package br.com.supermercadoatalaia.apprcm.domain.enumerated;

import java.util.ArrayList;
import java.util.List;

public enum Loja {
    VILA("Vila Margarida", "001"),
    QUARENTENARIO("Quarentenário", "002");

    private String bairro;
    private String codigo;

    Loja(String bairro, String codigo) {
        this.bairro = bairro;
        this.codigo = codigo;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCodigo() {
        return codigo;
    }

    public static Loja toEnum(String valor) {
        for(Loja loja : Loja.values()) {
            if(loja.getBairro().equals(valor) ||
                    loja.getCodigo().equals(valor)) {
                return loja;
            }
        }

        throw new EnumConstantNotPresentException(Loja.class, valor);
    }

    public static List<String> getBairros() {
        List<String> bairros = new ArrayList<>();

        for(Loja loja : Loja.values()) {
            bairros.add(loja.getBairro());
        }

        return bairros;
    }
}
