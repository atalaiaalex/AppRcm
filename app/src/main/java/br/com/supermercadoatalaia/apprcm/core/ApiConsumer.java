package br.com.supermercadoatalaia.apprcm.core;

import android.os.StrictMode;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiConsumer {
    private static String SERVER;
    public static String REST_COLETAS;
    public static String FORNECEDOR;
    public static String NUMERO_NOTA_FISCAL;
    public static String LANCAR;
    public static String REST_FORNECEDORES;
    public static String CNPJ;
    public static String VINCULO;
    public static String REST_PEDIDOS;
    public static String BAIXADOS_FORNECEDOR;
    public static String NOTA_FISCAL;
    public static String REST_PRODUTOS;
    public static String LOJA;
    public static String EAN;

    private final ConfigApp configApp;

    private HttpURLConnection httpURLConnection;
    private int HttpCodeResposta;

    public ApiConsumer(ConfigApp configApp) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.configApp = configApp;
    }

    public void carregarConfiguracao() throws IOException {
        SERVER = configApp.lerTxt();
        REST_COLETAS = SERVER + "/coletas";
        FORNECEDOR = "/fornecedor/";
        NUMERO_NOTA_FISCAL = "/numero_nota_fiscal/";
        LANCAR = "/lancar/";
        REST_FORNECEDORES = SERVER + "/fornecedores";
        CNPJ = "/cnpj/";
        VINCULO = "/vinculo/";
        REST_PEDIDOS = SERVER + "/pedidos";
        BAIXADOS_FORNECEDOR = "/baixados_fornecedor/";
        NOTA_FISCAL = "/nota_fiscal/";
        REST_PRODUTOS = SERVER + "/produtos";
        LOJA = "/loja/";
        EAN = "/ean/";

        //Log.i("Saida REST_COLETAS", REST_COLETAS);
    }

    public void iniciarConexao(String method, URL url) throws IOException {
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(method);
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
