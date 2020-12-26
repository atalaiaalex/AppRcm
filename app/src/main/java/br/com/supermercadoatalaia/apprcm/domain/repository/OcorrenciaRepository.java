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
import br.com.supermercadoatalaia.apprcm.domain.model.OcorrenciaFornecedor;

public class OcorrenciaRepository {

    private final ApiConsumer apiConsumer;

    public OcorrenciaRepository() {
        apiConsumer = new ApiConsumer();
        apiConsumer.carregarConfiguracao();
    }

    public List<OcorrenciaFornecedor> listarOcorrencias(Long fornecedorId) throws IOException {
        apiConsumer.iniciarConexao("GET", urlFornecedorIdOcorrencias(fornecedorId));
        apiConsumer.addCabecalho("Accept", "application/json");

        List<OcorrenciaFornecedor> ocorrencias;
        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
            ocorrencias = instanciarListaOcorrencia(jsonReader);
        } else {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return ocorrencias;
    }

    private URL urlFornecedorIdOcorrencias(Long id) throws MalformedURLException {
        return new URL(
                ApiConsumer.REST_FORNECEDORES +
                        "/" + id +
                        ApiConsumer.OCORRENCIAS_PENDENTE
        );
    }

    private List<OcorrenciaFornecedor> instanciarListaOcorrencia(JsonReader jsonReader) throws IOException {
        List<OcorrenciaFornecedor> ocorrencias = new ArrayList<>();

        jsonReader.beginArray();
        while(jsonReader.hasNext()) {
            ocorrencias.add(instanciarOcorrencia(jsonReader));
        }
        jsonReader.endArray();

        jsonReader.close();

        return ocorrencias;
    }

    private OcorrenciaFornecedor instanciarOcorrencia(JsonReader jsonReader) throws IOException {
        String mensagem = "";
        Long usuarioId = 0L;

        jsonReader.beginObject();
        while(jsonReader.hasNext()) {
            //Log.i("Json da ocorrencia", jsonReader.nextName());
            String key = jsonReader.nextName();

            if(key.equals("mensagem") && jsonReader.peek() != JsonToken.NULL) {
                mensagem = jsonReader.nextString();
            } else if(key.equals("usuarioId") && jsonReader.peek() != JsonToken.NULL) {
                usuarioId = jsonReader.nextLong();
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        return new OcorrenciaFornecedor(
                mensagem,
                usuarioId
        );
    }
}
