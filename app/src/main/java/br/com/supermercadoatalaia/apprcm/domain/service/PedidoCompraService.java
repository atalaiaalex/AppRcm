package br.com.supermercadoatalaia.apprcm.domain.service;

import java.util.List;

import br.com.supermercadoatalaia.apprcm.domain.model.PedidoCompra;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface PedidoCompraService {

    String END_POINT = "v1/pedidos";

    @GET(END_POINT+"/{id}")
    Call<PedidoCompra> buscarPorId(@Header("Authorization") String token,
                                   @Path("id") Long id);

    @GET(END_POINT+"/baixados_fornecedor/{fornecedorId}")
    Call<List<PedidoCompra>> listarBaixadosPorFornecedor(@Header("Authorization") String token,
                                                         @Path("fornecedorId") Long fornecedorId);

    @GET(END_POINT+"/baixados_fornecedor/{fornecedorId}/numero_nota_fiscal/{notaFiscalBaixada}")
    Call<List<PedidoCompra>> buscarPorFornecedorNf(@Header("Authorization") String token,
                                                  @Path("fornecedorId") Long fornecedorId,
                                                  @Path("notaFiscalBaixada") Long notaFiscalBaixada);

    @GET(END_POINT+"/detalhe/{produtoId}/loja/{unidade}")
    Call<Double> getQuantidadePedido(@Header("Authorization") String token,
                                     @Path("produtoId") Long produtoId,
                                     @Path("unidade") String unidade);
}
