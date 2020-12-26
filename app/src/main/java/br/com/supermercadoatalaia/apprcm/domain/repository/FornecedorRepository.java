package br.com.supermercadoatalaia.apprcm.domain.repository;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.supermercadoatalaia.apprcm.core.ApiConsumer;
import br.com.supermercadoatalaia.apprcm.core.HttpResposta;
import br.com.supermercadoatalaia.apprcm.core.exception.ApiException;
import br.com.supermercadoatalaia.apprcm.domain.model.Fornecedor;

public class FornecedorRepository {
    private final ApiConsumer apiConsumer;

    public FornecedorRepository() {
        apiConsumer = new ApiConsumer();
        apiConsumer.carregarConfiguracao();
    }

    public Fornecedor buscar(Long id) throws IOException {
        apiConsumer.iniciarConexao("GET", urlFornecedorId(id));
        apiConsumer.addCabecalho("Accept", "application/json");

        Fornecedor fornecedor;
        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
            fornecedor = instanciarFornecedor(jsonReader, true);
        } else {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return fornecedor;
    }

    public Fornecedor buscar(String cnpjCpf) throws IOException {
        apiConsumer.iniciarConexao("GET", urlFornecedorCnpjCpf(cnpjCpf));
        apiConsumer.addCabecalho("Accept", "application/json");

        Fornecedor fornecedor;
        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
            fornecedor = instanciarFornecedor(jsonReader, true);
        } else {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return fornecedor;
    }

    public List<Fornecedor> listar(Long vinculoCodigo) throws IOException {
        apiConsumer.iniciarConexao("GET", urlFornecedorVinculo(vinculoCodigo));
        apiConsumer.addCabecalho("Accept", "application/json");

        List<Fornecedor> fornecedores;
        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
            fornecedores = instanciarListaFornecedor(jsonReader);
        } else {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return fornecedores;
    }

    private URL urlFornecedorId(Long id) throws MalformedURLException {
        return new URL(
                ApiConsumer.REST_FORNECEDORES +
                        "/" + id
        );
    }

    private URL urlFornecedorCnpjCpf(String cnpjCpf) throws MalformedURLException {
        return new URL(
                ApiConsumer.REST_FORNECEDORES +
                        ApiConsumer.CNPJ +
                        cnpjCpf
        );
    }

    private URL urlFornecedorVinculo(Long vinculoCodigo) throws MalformedURLException {
        return new URL(
                ApiConsumer.REST_FORNECEDORES +
                        ApiConsumer.VINCULO +
                        vinculoCodigo
        );
    }

    private List<Fornecedor> instanciarListaFornecedor(JsonReader jsonReader) throws IOException {
        List<Fornecedor> fornecedores = new ArrayList<>();

        jsonReader.beginArray();
        while(jsonReader.hasNext()) {
            fornecedores.add(instanciarFornecedor(jsonReader, false));
        }
        jsonReader.endArray();
        jsonReader.close();

        return fornecedores;
    }

    private Fornecedor instanciarFornecedor(JsonReader jsonReader, boolean unico) throws IOException {
        Long id = 0L;
        String nome = "";
        String razaoSocial = "";
        String cnpjCpf = "";
        String situacao = "";
        Long vinculoCodigo = 0L;

        jsonReader.beginObject();
        while(jsonReader.hasNext()) {
            String key = jsonReader.nextName();
            if(key.equals("id")) {
                id = jsonReader.nextLong();
            } else if(key.equals("nome") && jsonReader.peek() != JsonToken.NULL) {
                nome = jsonReader.nextString();
            } else if(key.equals("razaoSocial") && jsonReader.peek() != JsonToken.NULL) {
                razaoSocial = jsonReader.nextString();
            } else if(key.equals("cnpjCpf") && jsonReader.peek() != JsonToken.NULL) {
                cnpjCpf = jsonReader.nextString();
            } else if(key.equals("situacao") && jsonReader.peek() != JsonToken.NULL) {
                situacao = jsonReader.nextString();
            } else if(key.equals("vinculoCodigo") && jsonReader.peek() != JsonToken.NULL) {
                vinculoCodigo = jsonReader.nextLong();
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        if(unico) {
            jsonReader.close();
        }

        return new Fornecedor(
                id,
                nome,
                razaoSocial,
                cnpjCpf,
                situacao,
                vinculoCodigo
        );
    }
}
