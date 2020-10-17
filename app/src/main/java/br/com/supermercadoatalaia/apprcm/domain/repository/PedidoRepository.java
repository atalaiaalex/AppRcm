package br.com.supermercadoatalaia.apprcm.domain.repository;

import android.util.JsonReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.supermercadoatalaia.apprcm.core.ApiConsumer;
import br.com.supermercadoatalaia.apprcm.core.ConfigApp;
import br.com.supermercadoatalaia.apprcm.domain.model.Pedido;

public class PedidoRepository {
    private final ApiConsumer apiConsumer;

    public PedidoRepository(ConfigApp configApp) throws IOException {
        apiConsumer = new ApiConsumer(configApp);
        apiConsumer.carregarConfiguracao();
    }

    public Pedido buscar(Long id) throws IOException {
        apiConsumer.iniciarConexao("GET", urlPedidoId(id));
        apiConsumer.addCabecalho("Accept", "application/json");

        Pedido pedido = instanciarPedido(apiConsumer.getJsonReader(), true);
        apiConsumer.fecharConexao();

        return pedido;
    }

    public List<Pedido> listar(Long fornecedorId) throws IOException {
        apiConsumer.iniciarConexao("GET", urlPedidoBaixadosFornecedor(fornecedorId));
        apiConsumer.addCabecalho("Accept", "application/json");

        List<Pedido> pedidos = instanciarListaPedido(apiConsumer.getJsonReader());
        apiConsumer.fecharConexao();

        return pedidos;
    }

    public Pedido buscar(Long fornecedorId, Long notaFiscalBaixada) throws IOException {
        apiConsumer.iniciarConexao(
                "GET",
                urlPedidoBaixadosFornecedorNotaFiscal(
                        fornecedorId,
                        notaFiscalBaixada
                )
        );
        apiConsumer.addCabecalho("Accept", "application/json");

        Pedido pedido = instanciarPedido(apiConsumer.getJsonReader(), true);
        apiConsumer.fecharConexao();

        return pedido;
    }

    private URL urlPedidoId(Long id) throws MalformedURLException {
        return new URL(
                ApiConsumer.REST_PEDIDOS +
                        "/" + id
        );
    }

    private URL urlPedidoBaixadosFornecedor(Long fornecedorId) throws MalformedURLException {
        return new URL(
                ApiConsumer.REST_PEDIDOS +
                        ApiConsumer.BAIXADOS_FORNECEDOR +
                        fornecedorId
        );
    }

    private URL urlPedidoBaixadosFornecedorNotaFiscal(Long fornecedorId, Long notaFiscalBaixada)
            throws MalformedURLException {
        return new URL(
                ApiConsumer.REST_PEDIDOS +
                        ApiConsumer.BAIXADOS_FORNECEDOR +
                        fornecedorId +
                        ApiConsumer.NUMERO_NOTA_FISCAL +
                        notaFiscalBaixada
        );
    }

    private List<Pedido> instanciarListaPedido(JsonReader jsonReader) throws IOException {
        List<Pedido> pedidos = new ArrayList<>();

        jsonReader.beginArray();
        while(jsonReader.hasNext()) {
            pedidos.add(instanciarPedido(jsonReader, false));
        }
        jsonReader.endArray();
        jsonReader.close();

        return pedidos;
    }

    private Pedido instanciarPedido(JsonReader jsonReader, boolean unico) throws IOException {
        Long id = 0L;
        String transacao = "";
        String status = "";
        Long fornecedorId = 0L;
        Long notaFiscalBaixada = 0L;
        String transacaoEntrada = "";
        String unidade = "";

        jsonReader.beginObject();
        while(jsonReader.hasNext()) {
            String key = jsonReader.nextName();
            if(key.equals("id")) {
                id = jsonReader.nextLong();
            } else if(key.equals("transacao")) {
                transacao = jsonReader.nextString();
            } else if(key.equals("status")) {
                status = jsonReader.nextString();
            } else if(key.equals("fornecedorId")) {
                fornecedorId = jsonReader.nextLong();
            } else if(key.equals("notaFiscalBaixada")) {
                notaFiscalBaixada = jsonReader.nextLong();
            } else if(key.equals("transacaoEntrada")) {
                transacaoEntrada = jsonReader.nextString();
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

        return new Pedido(
            id,
            transacao,
            status,
            fornecedorId,
            notaFiscalBaixada,
            transacaoEntrada,
            unidade
        );
    }
}
