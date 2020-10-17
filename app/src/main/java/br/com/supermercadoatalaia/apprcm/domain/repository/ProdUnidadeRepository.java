package br.com.supermercadoatalaia.apprcm.domain.repository;

import android.util.JsonReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.supermercadoatalaia.apprcm.core.ApiConsumer;
import br.com.supermercadoatalaia.apprcm.core.ConfigApp;
import br.com.supermercadoatalaia.apprcm.domain.model.ProdUnidade;

public class ProdUnidadeRepository {
    private final ApiConsumer apiConsumer;

    public ProdUnidadeRepository(ConfigApp configApp) throws IOException {
        apiConsumer = new ApiConsumer(configApp);
        apiConsumer.carregarConfiguracao();
    }

    public ProdUnidade buscar(Long id, String unidade) throws IOException {
        apiConsumer.iniciarConexao("GET", urlProdutoIdUnidade(id, unidade));
        apiConsumer.addCabecalho("Accept", "application/json");

        ProdUnidade prodUnidade = instanciarProdUnidade(apiConsumer.getJsonReader(), true);
        apiConsumer.fecharConexao();

        return prodUnidade;
    }

    public ProdUnidade buscar(String ean, String unidade) throws IOException {
        apiConsumer.iniciarConexao("GET", urlProdutoEanUnidade(ean, unidade));
        apiConsumer.addCabecalho("Accept", "application/json");

        ProdUnidade prodUnidade = instanciarProdUnidade(apiConsumer.getJsonReader(), true);
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
                        "/" + ean +
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
            } else if(key.equals("ean")) {
                ean = jsonReader.nextString();
            } else if(key.equals("descricao")) {
                descricao = jsonReader.nextString();
            } else if(key.equals("qntNaEmb")) {
                qntNaEmb = jsonReader.nextDouble();
            } else if(key.equals("status")) {
                status = jsonReader.nextString();
            } else if(key.equals("diasValidadeMinima")) {
                diasValidadeMinima = jsonReader.nextInt();
            } else if(key.equals("unidade")) {
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
