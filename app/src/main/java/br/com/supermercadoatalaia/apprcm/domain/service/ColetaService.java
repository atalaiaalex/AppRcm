package br.com.supermercadoatalaia.apprcm.domain.service;

import java.util.List;

import br.com.supermercadoatalaia.apprcm.domain.model.Coleta;
import br.com.supermercadoatalaia.apprcm.domain.model.LancamentoColeta;
import br.com.supermercadoatalaia.apprcm.dto.help.MessageResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ColetaService {

    String END_POINT = "v1/coletas";

    @GET(END_POINT+"/{id}")
    Call<Coleta> buscarColeta(@Header("Authorization") String token,
                              @Path("id") Long id);

    @GET(END_POINT+"/fornecedor/{fornecedorId}")
    Call<List<Coleta>> listarColetasPorFornecedor(@Header("Authorization") String token,
                                                  @Path("fornecedorId") Long fornecedorId);

    @GET(END_POINT+"/nota_fiscal/{numero}")
    Call<List<Coleta>> listarColetasPorNotaFiscal(@Header("Authorization") String token,
                                                  @Path("numero") Long numero);

    @GET(END_POINT+"/fornecedor/{fornecedorId}/nota_fiscal/{numero}")
    Call<Coleta> listarColetasPorFornecedorENotaFiscal(@Header("Authorization") String token,
                                                             @Path("fornecedorId") Long fornecedorId,
                                                             @Path("numero") Long numero);

    @POST(END_POINT)
    Call<MessageResponse> salvarColeta(@Header("Authorization") String token,
                              @Body Coleta coleta);

    @PUT(END_POINT+"/{id}")
    Call<MessageResponse> atualizarColeta(@Header("Authorization") String token,
                                 @Path("id") Long id,
                                 @Body Coleta coleta);

    @DELETE(END_POINT+"/{id}")
    Call<Void> deletarColeta(@Header("Authorization") String token,
                                 @Path("id") Long id);

    @POST(END_POINT+"/{id}/lancar")
    Call<MessageResponse> salvarItem(@Header("Authorization") String token,
                                     @Path("id") Long id,
                                     @Body LancamentoColeta item);

    @PUT(END_POINT+"/{id}/lancar/{itemId}")
    Call<MessageResponse> atualizarItem(@Header("Authorization") String token,
                                 @Path("id") Long id,
                                 @Path("itemId") Long itemId,
                                 @Body LancamentoColeta item);

    @DELETE(END_POINT+"/{id}/lancar/{itemId}")
    Call<Void> deletarItem(@Header("Authorization") String token,
                           @Path("id") Long id,
                           @Path("itemId") Long itemId);
}
