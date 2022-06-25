package br.com.supermercadoatalaia.apprcm.exception;

public class RequestFailureException extends Exception {

    public RequestFailureException(String msg) {
        super(msg);
    }

    public RequestFailureException(String msg, Exception e) {
        super(msg, e);
    }
}
