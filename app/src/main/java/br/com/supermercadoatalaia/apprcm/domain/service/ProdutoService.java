package br.com.supermercadoatalaia.apprcm.domain.service;

import br.com.supermercadoatalaia.apprcm.domain.model.ProdUnidade;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ProdutoService {

    String END_POINT = "v1/produtos";

    @GET(END_POINT+"/{id}/loja/{unidade}")
    Call<ProdUnidade> buscarPorIdUnidade(@Header("Authorization") String token,
                                         @Path("id") Long id,
                                         @Path("unidade") String unidade);

    @GET(END_POINT+"/ean/{ean}/loja/{unidade}")
    Call<ProdUnidade> buscarPorEanUnidade(@Header("Authorization") String token,
                                          @Path("ean") String ean,
                                          @Path("unidade") String unidade);
}
