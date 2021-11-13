package br.com.supermercadoatalaia.apprcm.domain.service;

import java.util.List;

import br.com.supermercadoatalaia.apprcm.domain.model.Coleta;
import br.com.supermercadoatalaia.apprcm.domain.model.LancamentoColeta;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ColetaService {

    @GET("{id}")
    Call<Coleta> buscarColeta(@Header("Authorization") String auth,
                              @Path("id") Long id);

    @GET("fornecedor/{fornecedorId}")
    Call<List<Coleta>> listarColetasPorFornecedor(@Header("Authorization") String auth,
                                                  @Path("fornecedorId") Long fornecedorId);

    @GET("numero_nota_fiscal/{numeroNotaFiscal}")
    Call<List<Coleta>> listarColetasPorNotaFiscal(@Header("Authorization") String auth,
                                                  @Path("numeroNotaFiscal") Long numeroNotaFiscal);

    @GET("fornecedor/{fornecedorId}/numero_nota_fiscal/{numeroNotaFiscal}")
    Call<Coleta> listarColetasPorFornecedorENotaFiscal(@Header("Authorization") String auth,
                                                             @Path("fornecedorId") Long fornecedorId,
                                                             @Path("numeroNotaFiscal") Long numeroNotaFiscal);

    @POST()
    Call<Coleta> salvarColeta(@Header("Authorization") String auth,
                              @Body Coleta coleta);

    @PUT("{id}")
    Call<Coleta> atualizarColeta(@Header("Authorization") String auth,
                                 @Path("id") Long id,
                                 @Body Coleta coleta);

    @DELETE("{id}")
    Call<Coleta> deletarColeta(@Header("Authorization") String auth,
                                 @Path("id") Long id);

    @POST("{id}/lancar/")
    Call<LancamentoColeta> salvarItem(@Header("Authorization") String auth,
                            @Path("id") Long id,
                            @Body LancamentoColeta item);

    @PUT("{id}/lancar/{itemId}")
    Call<LancamentoColeta> atualizarItem(@Header("Authorization") String auth,
                                 @Path("id") Long id,
                                 @Path("id") Long itemId,
                                 @Body LancamentoColeta item);
}
