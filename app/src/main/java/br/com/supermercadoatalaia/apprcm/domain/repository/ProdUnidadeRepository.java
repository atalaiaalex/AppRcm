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
import br.com.supermercadoatalaia.apprcm.domain.model.ProdUnidade;

public class ProdUnidadeRepository {
    private final ApiConsumer apiConsumer;

    public ProdUnidadeRepository() {
        apiConsumer = new ApiConsumer();
        apiConsumer.carregarConfiguracao();
    }

    public ProdUnidade buscar(Long id, String unidade) throws IOException {
        apiConsumer.iniciarConexao("GET", urlProdutoIdUnidade(id, unidade));
        apiConsumer.addCabecalho("Accept", "application/json");

        ProdUnidade prodUnidade;
        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
            prodUnidade = instanciarProdUnidade(jsonReader, true);
        } else {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return prodUnidade;
    }

    public ProdUnidade buscar(String ean, String unidade) throws IOException {
        apiConsumer.iniciarConexao("GET", urlProdutoEanUnidade(ean, unidade));
        apiConsumer.addCabecalho("Accept", "application/json");

        ProdUnidade prodUnidade;
        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
            prodUnidade = instanciarProdUnidade(jsonReader, true);
        } else {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return prodUnidade;
    }

    private URL urlProdutoIdUnidade(Long id, String unidade) throws MalformedURLException {
        return new URL(
                ApiConsumer.REST_PRODUTOS +
                        "/" + id +
                        ApiConsumer.LOJA +
                        unidade
        );
    }

    private URL urlProdutoEanUnidade(String ean, String unidade) throws MalformedURLException {
        return new URL(
                ApiConsumer.REST_PRODUTOS +
                        ApiConsumer.EAN +
                        ean +
                        ApiConsumer.LOJA +
                        unidade
        );
    }

    private List<ProdUnidade> instanciarListaProdUnidade(JsonReader jsonReader) throws IOException {
        List<ProdUnidade> prodsUnidade = new ArrayList<>();

        jsonReader.beginArray();
        while(jsonReader.hasNext()) {
            prodsUnidade.add(instanciarProdUnidade(jsonReader, false));
        }
        jsonReader.endArray();
        jsonReader.close();

        return prodsUnidade;
    }

    private ProdUnidade instanciarProdUnidade(JsonReader jsonReader, boolean unico) throws IOException {
        Long id = 0L;
        String ean = "";
        String descricao = "";
        Double qntNaEmb = 0.0;
        String status = "";
        Integer diasValidadeMinima = 0;
        String unidade = "";

        jsonReader.beginObject();
        while(jsonReader.hasNext()) {
            String key = jsonReader.nextName();
            if(key.equals("id")) {
                id = jsonReader.nextLong();
            } else if(key.equals("ean") && jsonReader.peek() != JsonToken.NULL) {
                ean = jsonReader.nextString();
            } else if(key.equals("descricao") && jsonReader.peek() != JsonToken.NULL) {
                descricao = jsonReader.nextString();
            } else if(key.equals("qntNaEmb") && jsonReader.peek() != JsonToken.NULL) {
                qntNaEmb = jsonReader.nextDouble();
            } else if(key.equals("status") && jsonReader.peek() != JsonToken.NULL) {
                status = jsonReader.nextString();
            } else if(key.equals("diasValidadeMinima") && jsonReader.peek() != JsonToken.NULL) {
                diasValidadeMinima = jsonReader.nextInt();
            } else if(key.equals("unidade") && jsonReader.peek() != JsonToken.NULL) {
                unidade = jsonReader.nextString();
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        if(unico) {
            jsonReader.close();
        }

        return new ProdUnidade(
                id,
                ean,
                descricao,
                qntNaEmb,
                status,
                diasValidadeMinima,
                unidade
        );
    }
}
