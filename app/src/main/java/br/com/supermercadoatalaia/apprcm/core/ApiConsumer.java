package br.com.supermercadoatalaia.apprcm.core;

import android.content.Context;
import android.os.StrictMode;
import android.util.Base64;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.supermercadoatalaia.apprcm.LoginActivity;
import br.com.supermercadoatalaia.apprcm.domain.model.Usuario;

public class ApiConsumer {
    public static String LOGIN;
    public static String USUARIO;
    public static String LOGOUT;

    public static String SERVER;
    public static String REST_COLETAS;
    public static String REST_PRODUTOS;
    public static String REST_FORNECEDORES;
    public static String REST_PEDIDOS;
    public static String REST_COTACAO_PRODUTO;

    public static String FORNECEDOR;
    public static String NUMERO_NOTA_FISCAL;
    public static String OCORRENCIAS_PENDENTE;
    public static String LANCAR;
    public static String CNPJ;
    public static String VINCULO;
    public static String BAIXADOS_FORNECEDOR;
    public static String NOTA_FISCAL;
    public static String LOJA;
    public static String EAN;

    private HttpURLConnection httpURLConnection;
    private int HttpCodeResposta;

    public ApiConsumer() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        carregarConfiguracao();
    }

    private void carregarConfiguracao() {
        LOGIN = SERVER + "/";
        USUARIO = SERVER + "/login";
        LOGOUT = SERVER + "/logout";

        REST_COLETAS = SERVER + "/coletas";
        REST_PEDIDOS = SERVER + "/pedidos";
        REST_FORNECEDORES = SERVER + "/fornecedores";
        REST_PRODUTOS = SERVER + "/produtos";
        REST_COTACAO_PRODUTO = SERVER + "/produtos_cotacao";

        FORNECEDOR = "/fornecedor/";
        OCORRENCIAS_PENDENTE = "/ocorrencias_pendente";
        NUMERO_NOTA_FISCAL = "/numero_nota_fiscal/";
        LANCAR = "/lancar/";
        CNPJ = "/cnpj/";
        VINCULO = "/vinculo/";
        BAIXADOS_FORNECEDOR = "/baixados_fornecedor/";
        NOTA_FISCAL = "/nota_fiscal/";
        LOJA = "/loja/";
        EAN = "/ean/";
    }

    public void iniciarConexao(String method, URL url, Context context) throws IOException {
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(method);

        autenticar(SharedPrefManager.getInstance(context).getUsuario());
    }

    public void login(String method, URL url, Context context, Usuario usuario)
            throws IOException {
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(method);

        autenticar(usuario);
    }

    private void autenticar(Usuario usuario) {
        addCabecalho("Authorization", "Basic " +
                Base64.encodeToString((usuario.getUsername()+":"+usuario.getPassword())
                        .getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP)
        );
    }

    public void addCabecalho(String chave, String valor) {
        httpURLConnection.setRequestProperty(chave, valor);
    }

    public OutputStream getOutputStream() throws IOException {
        return httpURLConnection.getOutputStream();
    }

    public InputStream processarComResposta() throws IOException {
        InputStream inputStream;

        try {
            inputStream = httpURLConnection.getInputStream();
            HttpCodeResposta = httpURLConnection.getResponseCode();
        } catch (IOException e) {
            inputStream = httpURLConnection.getErrorStream();
            HttpCodeResposta = httpURLConnection.getResponseCode();
        }

        return inputStream;
    }

    private String htmlToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader( new InputStreamReader( inputStream ) );
        StringBuffer html = new StringBuffer();
        String linha = "";

        while( ( linha = reader.readLine() ) != null ) {
            html.append(linha);
        }

        return html.toString();
    }

    public HttpResposta getHttpResposta() {
        return HttpResposta.getHttpMensagem(HttpCodeResposta);
    }

    public JsonReader getJsonReader() throws IOException {
        return new JsonReader(
                new InputStreamReader(
                        this.processarComResposta(), "UTF-8"
                )
        );
    }

    public JsonWriter getJsonWriter() throws IOException {
        return new JsonWriter(
                new OutputStreamWriter(
                        this.getOutputStream(), "UTF-8"
                )
        );
    }

    public void fecharConexao() {
        httpURLConnection.disconnect();
    }
}
