package br.com.supermercadoatalaia.apprcm.domain.model;

import java.io.Serializable;
import java.util.Calendar;

public class OcorrenciaFornecedor implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mensagem;
    private Long usuarioId;

    public OcorrenciaFornecedor() {}

    public OcorrenciaFornecedor(String mensagem, Long usuarioId) {
        super();
        this.mensagem = mensagem;
        this.usuarioId = usuarioId;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
