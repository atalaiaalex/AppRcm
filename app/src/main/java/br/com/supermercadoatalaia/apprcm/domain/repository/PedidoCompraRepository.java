package br.com.supermercadoatalaia.apprcm.domain.repository;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import br.com.supermercadoatalaia.apprcm.config.RetrofitAtalaiaConfig;
import br.com.supermercadoatalaia.apprcm.core.SharedPrefManager;
import br.com.supermercadoatalaia.apprcm.domain.model.PedidoCompra;
import br.com.supermercadoatalaia.apprcm.domain.service.PedidoCompraService;
import br.com.supermercadoatalaia.apprcm.exception.RegistroNotFoundException;
import retrofit2.Call;
import retrofit2.Response;

public class PedidoCompraRepository {

    private final Context context;
    private final PedidoCompraService service;

    public PedidoCompraRepository(Context context) {
        this.service = new RetrofitAtalaiaConfig().getPedidoCompraService();
        this.context = context;
    }

    public PedidoCompra buscar(Long id) throws RegistroNotFoundException {
        Call<PedidoCompra> call = service.buscarPorId(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                id
        );

        Response<PedidoCompra> pedido;
        try {
            pedido = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar pedido", e);
        }

        if(pedido.isSuccessful()) {
            return pedido.body();
        }

        throw new RegistroNotFoundException("Pedido não encontrado");
    }

    public List<PedidoCompra> listar(Long fornecedorId) throws RegistroNotFoundException {
        Call<List<PedidoCompra>> call = service.listarBaixadosPorFornecedor(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                fornecedorId
        );

        Response<List<PedidoCompra>> pedidos;
        try {
            pedidos = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar pedido", e);
        }

        if(pedidos.isSuccessful()) {
            return pedidos.body();
        }

        throw new RegistroNotFoundException("Pedido não encontrado");
    }

    public List<PedidoCompra> buscar(Long fornecedorId, Long notaFiscalBaixada)
            throws RegistroNotFoundException {

        Call<List<PedidoCompra>> call = service.buscarPorFornecedorNf(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                fornecedorId,
                notaFiscalBaixada
        );

        Response<List<PedidoCompra>> pedidos;
        try {
            pedidos = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar pedido", e);
        }

        if(pedidos.isSuccessful()) {
            return pedidos.body();
        }

        throw new RegistroNotFoundException("Pedido não encontrado");
    }
}
