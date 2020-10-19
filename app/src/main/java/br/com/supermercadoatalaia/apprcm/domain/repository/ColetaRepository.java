package br.com.supermercadoatalaia.apprcm.domain.repository;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.supermercadoatalaia.apprcm.core.ApiConsumer;
import br.com.supermercadoatalaia.apprcm.core.ConfigApp;
import br.com.supermercadoatalaia.apprcm.core.HttpResposta;
import br.com.supermercadoatalaia.apprcm.domain.model.Coleta;
import br.com.supermercadoatalaia.apprcm.domain.model.LancamentoColeta;

public class ColetaRepository {
    private final ApiConsumer apiConsumer;

    public ColetaRepository(ConfigApp configApp) throws IOException {
        apiConsumer = new ApiConsumer(configApp);
        apiConsumer.carregarConfiguracao();
    }

    public Coleta buscar(Long id) throws IOException, ParseException {
        apiConsumer.iniciarConexao("GET", urlColetaId(id));
        apiConsumer.addCabecalho("Accept", "application/json");

        Coleta coleta = instanciarColeta(apiConsumer.getJsonReader(), true);
        apiConsumer.fecharConexao();

        return coleta;
    }

    public List<Coleta> listarPorFornecedor(Long fornecedorId) throws IOException, ParseException {
        apiConsumer.iniciarConexao("GET", urlColetaFornecedorId(fornecedorId));
        apiConsumer.addCabecalho("Accept", "application/json");

        List<Coleta> coletas = instanciarListaColeta(apiConsumer.getJsonReader());
        apiConsumer.fecharConexao();

        return coletas;
    }

    public List<Coleta> listarPorNf(Long numeroNotaFiscal) throws IOException, ParseException {
        apiConsumer.iniciarConexao("GET", urlColetaNumeroNotaFiscal(numeroNotaFiscal));
        apiConsumer.addCabecalho("Accept", "application/json");

        List<Coleta> coletas = instanciarListaColeta(apiConsumer.getJsonReader());
        apiConsumer.fecharConexao();

        return coletas;
    }

    public Coleta buscar(Long fornecedorId, Long numeroNotaFiscal)
            throws IOException, ParseException {
        apiConsumer.iniciarConexao("GET",
                urlColetaFornecedorIdNumeroNotaFiscal(fornecedorId, numeroNotaFiscal)
        );
        apiConsumer.addCabecalho("Accept", "application/json");

        Coleta coleta = instanciarColeta(apiConsumer.getJsonReader(), true);
        apiConsumer.fecharConexao();

        return coleta;

    }

    public Coleta salvar(Coleta coleta) throws IOException, ParseException {
        apiConsumer.iniciarConexao("POST",
                new URL(ApiConsumer.REST_COLETAS)
        );
        apiConsumer.addCabecalho("Content-Type", "application/json");
        apiConsumer.addCabecalho("Accept", "application/json");

        setColetaToApi(apiConsumer.getJsonWriter(), coleta);
        coleta = instanciarColeta(apiConsumer.getJsonReader(), true);
        apiConsumer.fecharConexao();

        return coleta;
    }

    public Coleta atualizar(Coleta coleta) throws IOException, ParseException {
        apiConsumer.iniciarConexao("PUT",
                urlColetaId(coleta.getId())
        );
        apiConsumer.addCabecalho("Content-Type", "application/json");
        apiConsumer.addCabecalho("Accept", "application/json");

        setColetaToApi(apiConsumer.getJsonWriter(), coleta);
        coleta = instanciarColeta(apiConsumer.getJsonReader(), true);
        apiConsumer.fecharConexao();

        return coleta;
    }

    public HttpResposta deletar(Coleta coleta) throws IOException {
        apiConsumer.iniciarConexao("DELETE",
                urlColetaId(coleta.getId())
        );

        apiConsumer.processarComResposta();//Precisei chamar para que o request foce efetuado

        apiConsumer.fecharConexao();

        return apiConsumer.getHttpResposta();
    }

    public LancamentoColeta salvarItem(Coleta coleta, LancamentoColeta item)
            throws IOException, ParseException {
        apiConsumer.iniciarConexao("POST",
                urlColetaLancar(coleta.getId())
        );
        apiConsumer.addCabecalho("Content-Type", "application/json");
        apiConsumer.addCabecalho("Accept", "application/json");

        setLancamentoColetaToApi(apiConsumer.getJsonWriter(), item, true);
        item = instanciarLancamentoColeta(apiConsumer.getJsonReader());
        apiConsumer.fecharConexao();

        return item;
    }

    public LancamentoColeta atualizarItem(Coleta coleta, LancamentoColeta item)
            throws IOException, ParseException {
        apiConsumer.iniciarConexao("PUT",
                urlColetaLancarId(coleta.getId(), item.getId())
        );
        apiConsumer.addCabecalho("Content-Type", "application/json");
        apiConsumer.addCabecalho("Accept", "application/json");

        setLancamentoColetaToApi(apiConsumer.getJsonWriter(), item, true);
        item = instanciarLancamentoColeta(apiConsumer.getJsonReader());
        apiConsumer.fecharConexao();

        return item;
    }

    public HttpResposta deletarItem(Coleta coleta, LancamentoColeta item) throws IOException {
        apiConsumer.iniciarConexao("DELETE",
                urlColetaLancarId(coleta.getId(), item.getId())
        );

        apiConsumer.processarComResposta();//Precisei chamar para que o request foce efetuado

        apiConsumer.fecharConexao();

        return apiConsumer.getHttpResposta();
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
        jsonWriter.name("pedidoId").value(coleta.getPedidoId());
        jsonWriter.name("unidade").value(coleta.getUnidade());
        if(coleta.getDataMovimento() != null) {
            jsonWriter.name("dataMovimento").value(coleta.getDataMovimento().toString());
        }
        if(coleta.getDataAlteracao() != null) {
            jsonWriter.name("dataAlteracao").value(coleta.getDataAlteracao().toString());
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

    private void setListaLancamentoColetaToApi(JsonWriter jsonWriter, List<LancamentoColeta> itens)
            throws IOException {

        for(LancamentoColeta item : itens) {
            jsonWriter.beginArray();
            setLancamentoColetaToApi(jsonWriter, item, false);
            jsonWriter.endArray();
        }
        jsonWriter.close();
    }

    private void setLancamentoColetaToApi(JsonWriter jsonWriter, LancamentoColeta item, boolean unico)
            throws IOException {

        jsonWriter.setIndent("  ");
        jsonWriter.beginObject();
        jsonWriter.name("id").value(item.getId());
        jsonWriter.name("produtoId").value(item.getProdutoId());
        jsonWriter.name("qntNaEmb").value(item.getQntNaEmb());
        jsonWriter.name("qntEmb").value(item.getQntEmb());
        jsonWriter.name("qntTotal").value(item.getQntTotal());
        if(item.getDataAlteracao() != null) {
            jsonWriter.name("dataAlteracao").value(item.getDataAlteracao().toString());
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
        Long pedidoId = 0L;
        String unidade = "";
        List<LancamentoColeta> itens = new ArrayList<>();
        Calendar dataMovimento = null;
        Calendar dataAlteracao = null;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String key = jsonReader.nextName();
            if (key.equals("id")) {
                id = jsonReader.nextLong();
            } else if(key.equals("fornecedorId")) {
                fornecedorId = jsonReader.nextLong();
            } else if(key.equals("numeroNotaFiscal")) {
                numeroNotaFiscal = jsonReader.nextLong();
            } else if(key.equals("pedidoId")) {
                pedidoId = jsonReader.nextLong();
            } else if(key.equals("unidade")) {
                unidade = jsonReader.nextString();
            } else if(key.equals("dataMovimento")) {
                dataMovimento.setTime(
                        new SimpleDateFormat().parse(jsonReader.nextString())
                );
            } else if(key.equals("dataAlteracao")) {
                dataAlteracao.setTime(
                        new SimpleDateFormat().parse(jsonReader.nextString())
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
                pedidoId,
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
        Double qntNaEmb = 0.0;
        Double qntEmb = 0.0;
        Double qntTotal = 0.0;
        Calendar vencimento = null;
        Integer diasValidadeMinima = 0;
        Calendar dataAlteracao = null;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String key = jsonReader.nextName();
            if(key.equals("id")) {
                id = jsonReader.nextLong();
            } else if(key.equals("produtoId")) {
                produtoId = jsonReader.nextLong();
            } else if(key.equals("qntNaEmb")) {
                qntNaEmb = jsonReader.nextDouble();
            } else if(key.equals("qntEmb")) {
                qntEmb = jsonReader.nextDouble();
            } else if(key.equals("qntTotal")) {
                qntTotal = jsonReader.nextDouble();
            } else if(key.equals("vencimento")) {
                vencimento.setTime(
                        new SimpleDateFormat().parse(jsonReader.nextString())
                );
            } else if(key.equals("diasValidadeMinima")) {
                diasValidadeMinima = jsonReader.nextInt();
            } else if(key.equals("dataAlteracao")) {
                dataAlteracao.setTime(
                    new SimpleDateFormat().parse(jsonReader.nextString())
            );
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        return new LancamentoColeta(
                id,
                produtoId,
                qntNaEmb,
                qntEmb,
                qntTotal,
                vencimento,
                diasValidadeMinima,
                dataAlteracao
        );
    }
}
