package br.com.supermercadoatalaia.apprcm.domain.service;

import br.com.supermercadoatalaia.apprcm.dto.request.AutenticarSessao;
import br.com.supermercadoatalaia.apprcm.dto.response.AutenticacaoResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginFlexService {

    @POST("v1.1/auth")
    Call<AutenticacaoResponse> login(@Body AutenticarSessao autenticarSessao);

    @GET("v1.1/logout")
    Call<Void> logout(@Header("token") String token,
                      @Header("Cookie") String cookie);
}
