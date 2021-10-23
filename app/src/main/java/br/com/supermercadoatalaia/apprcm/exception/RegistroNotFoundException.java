package br.com.supermercadoatalaia.apprcm.exception;

public class RegistroNotFoundException extends RuntimeException {

    public RegistroNotFoundException(String msg) {
        super(msg);
    }

    public RegistroNotFoundException(String msg, Exception e) {
        super(msg, e);
    }
}
