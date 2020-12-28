package br.com.supermercadoatalaia.apprcm.domain.repository;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.supermercadoatalaia.apprcm.core.ApiConsumer;
import br.com.supermercadoatalaia.apprcm.core.HttpResposta;
import br.com.supermercadoatalaia.apprcm.core.exception.ApiException;
import br.com.supermercadoatalaia.apprcm.domain.model.Coleta;
import br.com.supermercadoatalaia.apprcm.domain.model.LancamentoColeta;

public class ColetaRepository {

    private static final String FORMATO_DATA_HORA = "yyyy-MM-dd'T'HH:mm:ss.SSSSS";
    private static final String FORMATO_DATA = "yyyy-MM-dd";

    private final ApiConsumer apiConsumer;

    public ColetaRepository() {
        apiConsumer = new ApiConsumer();
        apiConsumer.carregarConfiguracao();
    }

    public Coleta buscar(Long id) throws ApiException, IOException, ParseException {
        apiConsumer.iniciarConexao("GET", urlColetaId(id));
        apiConsumer.addCabecalho("Accept", "application/json");

        Coleta coleta;
        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
            coleta = instanciarColeta(jsonReader, true);
        } else {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return coleta;
    }

    public List<Coleta> listarPorFornecedor(Long fornecedorId)
            throws ApiException, IOException, ParseException {
        apiConsumer.iniciarConexao("GET", urlColetaFornecedorId(fornecedorId));
        apiConsumer.addCabecalho("Accept", "application/json");

        List<Coleta> coletas;
        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
            coletas = instanciarListaColeta(apiConsumer.getJsonReader());
        } else {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return coletas;
    }

    public List<Coleta> listarPorNf(Long numeroNotaFiscal)
            throws ApiException, IOException, ParseException {
        apiConsumer.iniciarConexao("GET", urlColetaNumeroNotaFiscal(numeroNotaFiscal));
        apiConsumer.addCabecalho("Accept", "application/json");

        List<Coleta> coletas;
        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
            coletas = instanciarListaColeta(apiConsumer.getJsonReader());
        } else {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return coletas;
    }

    public Coleta buscar(Long fornecedorId, Long numeroNotaFiscal)
            throws ApiException, ParseException, IOException {
        apiConsumer.iniciarConexao("GET",
                urlColetaFornecedorIdNumeroNotaFiscal(fornecedorId, numeroNotaFiscal)
        );
        apiConsumer.addCabecalho("Accept", "application/json");

        Coleta coleta;
        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
            coleta = instanciarColeta(jsonReader, true);
        } else {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return coleta;

    }

    public Coleta salvar(Coleta coleta) throws ApiException, IOException, ParseException {
        apiConsumer.iniciarConexao("POST",
                new URL(ApiConsumer.REST_COLETAS)
        );
        apiConsumer.addCabecalho("Content-Type", "application/json");
        apiConsumer.addCabecalho("Accept", "application/json");

        setColetaToApi(apiConsumer.getJsonWriter(), coleta);

        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
            coleta = instanciarColeta(jsonReader, true);
        } else {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return coleta;
    }

    public Coleta atualizar(Coleta coleta) throws ApiException, IOException, ParseException {
        apiConsumer.iniciarConexao("PUT",
                urlColetaId(coleta.getId())
        );
        apiConsumer.addCabecalho("Content-Type", "application/json");
        apiConsumer.addCabecalho("Accept", "application/json");

        setColetaToApi(apiConsumer.getJsonWriter(), coleta);

        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
            coleta = instanciarColeta(jsonReader, true);
        } else {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return coleta;
    }

    public HttpResposta deletar(Coleta coleta) throws ApiException, IOException {
        apiConsumer.iniciarConexao("DELETE",
                urlColetaId(coleta.getId())
        );

        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() > 300) {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return httpResposta;
    }

    public LancamentoColeta salvarItem(Coleta coleta, LancamentoColeta item)
            throws ApiException, IOException, ParseException {
        apiConsumer.iniciarConexao("POST",
                urlColetaLancar(coleta.getId())
        );
        apiConsumer.addCabecalho("Content-Type", "application/json");
        apiConsumer.addCabecalho("Accept", "application/json");

        setLancamentoColetaToApi(apiConsumer.getJsonWriter(), item, true);

        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
            item = instanciarLancamentoColeta(jsonReader);
        } else {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return item;
    }

    public LancamentoColeta atualizarItem(Coleta coleta, LancamentoColeta item)
            throws ApiException, IOException, ParseException {
        apiConsumer.iniciarConexao("PUT",
                urlColetaLancarId(coleta.getId(), item.getId())
        );
        apiConsumer.addCabecalho("Content-Type", "application/json");
        apiConsumer.addCabecalho("Accept", "application/json");

        setLancamentoColetaToApi(apiConsumer.getJsonWriter(), item, true);

        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
            item = instanciarLancamentoColeta(jsonReader);
        } else {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return item;
    }

    public HttpResposta deletarItem(Coleta coleta, LancamentoColeta item)
            throws ApiException, IOException {
        apiConsumer.iniciarConexao("DELETE",
                urlColetaLancarId(coleta.getId(), item.getId())
        );

        JsonReader jsonReader = apiConsumer.getJsonReader();
        HttpResposta httpResposta = apiConsumer.getHttpResposta();

        if(httpResposta.getCode() > 300) {
            throw new ApiException(httpResposta, jsonReader);
        }

        apiConsumer.fecharConexao();

        return httpResposta;
    }

    private URL urlColetaId(Long id) throws MalformedURLException {
        return new URL(
                ApiConsumer.REST_COLETAS +
                        "/" + id
        );
    }

    private URL urlColetaFornecedorId(Long fornecedorId) throws MalformedURLException {
        return new URL(
                ApiConsumer.REST_COLETAS +
                        ApiConsumer.FORNECEDOR +
                        fornecedorId
        );
    }

    private URL urlColetaNumeroNotaFiscal(Long numeroNotaFiscal) throws MalformedURLException {
        return new URL(
                ApiConsumer.REST_COLETAS +
                        ApiConsumer.NUMERO_NOTA_FISCAL +
                        numeroNotaFiscal
        );
    }

    private URL urlColetaFornecedorIdNumeroNotaFiscal(Long fornecedorId, Long numeroNotaFiscal)
            throws MalformedURLException {
        return new URL(
                ApiConsumer.REST_COLETAS +
                        ApiConsumer.FORNECEDOR +
                        fornecedorId +
                        ApiConsumer.NUMERO_NOTA_FISCAL +
                        numeroNotaFiscal
        );
    }

    private URL urlColetaLancar(Long id) throws MalformedURLException {
        return new URL(
                ApiConsumer.REST_COLETAS +
                        "/" + id +
                        ApiConsumer.LANCAR
        );
    }

    private URL urlColetaLancarId(Long id, Long itemId) throws MalformedURLException {
        Log.i("EndPoint-LançarItem", ApiConsumer.REST_COLETAS +
                "/" + id +
                ApiConsumer.LANCAR +
                itemId);
        return new URL(
                ApiConsumer.REST_COLETAS +
                        "/" + id +
                        ApiConsumer.LANCAR +
                        itemId
        );
    }

    private void setColetaToApi(JsonWriter jsonWriter, Coleta coleta)
            throws IOException {

        jsonWriter.setIndent("  ");
        jsonWriter.beginObject();
        jsonWriter.name("id").value(coleta.getId());
        jsonWriter.name("fornecedorId").value(coleta.getFornecedorId());
        jsonWriter.name("numeroNotaFiscal").value(coleta.getNumeroNotaFiscal());
        jsonWriter.name("serie").value(coleta.getSerie());
        jsonWriter.name("pedidosId");
        setListPedidosId(jsonWriter, coleta.getPedidosId());
        jsonWriter.name("unidade").value(coleta.getUnidade());
        if(coleta.getDataMovimento() != null) {
            jsonWriter.name("dataMovimento").value(
                    new SimpleDateFormat(FORMATO_DATA_HORA).format(coleta.getDataMovimento().getTime())
            );
        }
        if(coleta.getDataAlteracao() != null) {
            jsonWriter.name("dataAlteracao").value(
                    new SimpleDateFormat(FORMATO_DATA_HORA).format(coleta.getDataAlteracao().getTime())
            );
        }
        if(coleta.getItens().isEmpty()) {
            jsonWriter.name("itens").nullValue();
        } else {
            jsonWriter.name("itens");
            setListaLancamentoColetaToApi(jsonWriter, coleta.getItens());
        }
        jsonWriter.endObject();
        jsonWriter.close();
    }

    private void setListPedidosId(JsonWriter jsonWriter, Set<Long> pedidosId)
            throws IOException {

        jsonWriter.beginArray();
        for(Long id : pedidosId) {
            jsonWriter.value(id);
        }
        jsonWriter.endArray();
    }

    private void setListaLancamentoColetaToApi(JsonWriter jsonWriter, List<LancamentoColeta> itens)
            throws IOException {

        jsonWriter.beginArray();
        for(LancamentoColeta item : itens) {
            setLancamentoColetaToApi(jsonWriter, item, false);
        }
        jsonWriter.endArray();
    }

    private void setLancamentoColetaToApi(JsonWriter jsonWriter, LancamentoColeta item, boolean unico)
            throws IOException {

        jsonWriter.setIndent("  ");
        jsonWriter.beginObject();
        jsonWriter.name("id").value(item.getId());
        jsonWriter.name("produtoId").value(item.getProdutoId());
        jsonWriter.name("produtoDescricao").value(item.getProdutoDescricao());
        jsonWriter.name("produtoEan").value(item.getProdutoEan());
        jsonWriter.name("qntNaEmb").value(item.getQntNaEmb());
        jsonWriter.name("qntEmb").value(item.getQntEmb());
        jsonWriter.name("qntTotal").value(item.getQntTotal());
        if(item.getVencimento() != null) {
            jsonWriter.name("vencimento").value(
                    new SimpleDateFormat(FORMATO_DATA).format(item.getVencimento().getTime())
            );
        }
        jsonWriter.name("diasValidadeMinima").value(item.getDiasValidadeMinima());
        if(item.getDataAlteracao() != null) {
            jsonWriter.name("dataAlteracao").value(
                    new SimpleDateFormat(FORMATO_DATA_HORA).format(item.getDataAlteracao().getTime())
            );
        }
        jsonWriter.endObject();

        if(unico) {
            jsonWriter.close();
        }
    }

    private List<Coleta> instanciarListaColeta(JsonReader jsonReader)
            throws IOException, ParseException {
        List<Coleta> coletas = new ArrayList<>();

        jsonReader.beginArray();
        while(jsonReader.hasNext()){
            coletas.add(instanciarColeta(jsonReader, false));
        }
        jsonReader.endArray();
        jsonReader.close();

        return coletas;
    }

    private Coleta instanciarColeta(JsonReader jsonReader, boolean unico)
            throws IOException, ParseException {
        Long id = 0L;
        Long fornecedorId = 0L;
        Long numeroNotaFiscal = 0L;
        String serie = "";
        Set<Long> pedidosId = new HashSet<>();
        String unidade = "";
        List<LancamentoColeta> itens = new ArrayList<>();
        Calendar dataMovimento = Calendar.getInstance();
        Calendar dataAlteracao = Calendar.getInstance();

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String key = jsonReader.nextName();
            if (key.equals("id")) {
                id = jsonReader.nextLong();
            } else if(key.equals("fornecedorId") && jsonReader.peek() != JsonToken.NULL) {
                fornecedorId = jsonReader.nextLong();
            } else if(key.equals("numeroNotaFiscal") && jsonReader.peek() != JsonToken.NULL) {
                numeroNotaFiscal = jsonReader.nextLong();
            } else if(key.equals("serie") && jsonReader.peek() != JsonToken.NULL) {
                serie = jsonReader.nextString();
            } else if(key.equals("pedidosId") && jsonReader.peek() != JsonToken.NULL) {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    pedidosId.add(jsonReader.nextLong());
                }
                jsonReader.endArray();
            } else if(key.equals("unidade") && jsonReader.peek() != JsonToken.NULL) {
                unidade = jsonReader.nextString();
            } else if(key.equals("dataMovimento") && jsonReader.peek() != JsonToken.NULL) {
                //Log.i("DataMovimento API", new Date().toString()); //Devo transformar a data para GMT+00:00, e por no banco, cada aplicativo
                    //deve se responsabilizar por adicionar o seu GMT tanto para ler quanto para escrever as datas.
                dataMovimento.setTime(
                        new SimpleDateFormat(FORMATO_DATA_HORA).parse(jsonReader.nextString())
                );
            } else if(key.equals("dataAlteracao") && jsonReader.peek() != JsonToken.NULL) {
                dataAlteracao.setTime(
                        new SimpleDateFormat(FORMATO_DATA_HORA).parse(jsonReader.nextString())
                );
            } else if(key.equals("itens") && jsonReader.peek() != JsonToken.NULL) {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    itens.add(instanciarLancamentoColeta(jsonReader));
                }
                jsonReader.endArray();
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        if(unico) {
            jsonReader.close();
        }

        return new Coleta(
                id,
                fornecedorId,
                numeroNotaFiscal,
                serie,
                pedidosId,
                itens,
                dataMovimento,
                dataAlteracao,
                unidade
        );
    }

    private LancamentoColeta instanciarLancamentoColeta(JsonReader jsonReader)
            throws IOException, ParseException {
        Long id = 0L;
        Long produtoId = 0L;
        String produtoDescricao = "";
        String produtoEan = "";
        Double qntNaEmb = 0.0;
        Double qntEmb = 0.0;
        Double qntTotal = 0.0;
        Calendar vencimento = Calendar.getInstance();
        Integer diasValidadeMinima = 0;
        Calendar dataAlteracao = Calendar.getInstance();

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String key = jsonReader.nextName();
            if(key.equals("id")) {
                id = jsonReader.nextLong();
            } else if(key.equals("produtoId") && jsonReader.peek() != JsonToken.NULL) {
                produtoId = jsonReader.nextLong();
            } else if(key.equals("produtoDescricao") && jsonReader.peek() != JsonToken.NULL) {
                produtoDescricao = jsonReader.nextString();
            } else if(key.equals("produtoEan") && jsonReader.peek() != JsonToken.NULL) {
                produtoEan = jsonReader.nextString();
            } else if(key.equals("qntNaEmb") && jsonReader.peek() != JsonToken.NULL) {
                qntNaEmb = jsonReader.nextDouble();
            } else if(key.equals("qntEmb") && jsonReader.peek() != JsonToken.NULL) {
                qntEmb = jsonReader.nextDouble();
            } else if(key.equals("qntTotal") && jsonReader.peek() != JsonToken.NULL) {
                qntTotal = jsonReader.nextDouble();
            } else if(key.equals("vencimento") && jsonReader.peek() != JsonToken.NULL) {
                vencimento.setTime(
                        new SimpleDateFormat(FORMATO_DATA).parse(jsonReader.nextString())
                );
            } else if(key.equals("diasValidadeMinima") && jsonReader.peek() != JsonToken.NULL) {
                diasValidadeMinima = jsonReader.nextInt();
            } else if(key.equals("dataAlteracao") && jsonReader.peek() != JsonToken.NULL) {
                dataAlteracao.setTime(
                    new SimpleDateFormat(FORMATO_DATA_HORA).parse(jsonReader.nextString())
            );
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        return new LancamentoColeta(
                id,
                produtoId,
                produtoDescricao,
                qntNaEmb,
                qntEmb,
                qntTotal,
                vencimento,
                diasValidadeMinima,
                produtoEan,
                dataAlteracao
        );
    }
}
