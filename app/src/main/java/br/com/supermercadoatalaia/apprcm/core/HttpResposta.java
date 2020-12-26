package br.com.supermercadoatalaia.apprcm.core;

public enum HttpResposta {
    SUCESSO(200, "Sucesso"),
    INCLUIDO(201, "Registro incluído"),
    SEM_CONTEUDO(204, "Sem conteúdo"),
    ERRO(400, "Solicitação incorreta"),
    NAO_AUTORIZADO(401, "Não autorização"),
    SEM_AUTORIZACAO(403, "Sem autorização"),
    NAO_ENCONTRADO(404, "Não encontrado"),
    METODO_NAO_PERMITIDO(405, "Método não permitido"),
    NAO_ACEITAVEL(406, "Requisição não aceita"),
    ERRO_SERVIDOR(500, "Erro no servidor"),
    ERRO_SEM_TRADUCAO(-1, "Erro"),
    RESPOSTA_SEM_TRADUCAO(-2, "Resposta");

    /*
    1×× Informational
100 Continue
101 Switching Protocols
102 Processing

202 Accepted
203 Non-authoritative Information
205 Reset Content
206 Partial Content
207 Multi-Status
208 Already Reported
226 IM Used

    3×× Redirection
300 Multiple Choices
301 Moved Permanently
302 Found
303 See Other
304 Not Modified
305 Use Proxy
307 Temporary Redirect
308 Permanent Redirect

402 Payment Required
407 Proxy Authentication Required
408 Request Timeout
409 Conflict
410 Gone
411 Length Required
412 Precondition Failed
413 Payload Too Large
414 Request-URI Too Long
415 Unsupported Media Type
416 Requested Range Not Satisfiable
417 Expectation Failed
418 I'm a teapot
421 Misdirected Request
422 Unprocessable Entity
423 Locked
424 Failed Dependency
426 Upgrade Required
428 Precondition Required
429 Too Many Requests
431 Request Header Fields Too Large
444 Connection Closed Without Response
451 Unavailable For Legal Reasons
499 Client Closed Request

501 Not Implemented
502 Bad Gateway
503 Service Unavailable
504 Gateway Timeout
505 HTTP Version Not Supported
506 Variant Also Negotiates
507 Insufficient Storage
508 Loop Detected
510 Not Extended
511 Network Authentication Required
599 Network Connect Timeout Error
     */

    private int code;
    private String mensagem;

    HttpResposta(int code, String mensagem) {
        this.code = code;
        this.mensagem = mensagem;
    }

    public int getCode() {
        return this.code;
    }

    public String getMensagem() {
        return this.mensagem;
    }

    public static HttpResposta getHttpMensagem(int code) {
        for(HttpResposta resp : HttpResposta.values()) {
            if(resp.code == code){
                return resp;
            }
        }

        if(code > 400){
            return ERRO_SEM_TRADUCAO;
        }

        return RESPOSTA_SEM_TRADUCAO;
    }
}
