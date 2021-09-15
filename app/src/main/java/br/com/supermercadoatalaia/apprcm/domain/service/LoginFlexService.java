package br.com.supermercadoatalaia.apprcm.domain.service;

import br.com.supermercadoatalaia.apprcm.domain.model.AutenticarSessao;
import br.com.supermercadoatalaia.apprcm.domain.model.RespostaAutenticacao;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginFlexService {

    @POST("v1.1/auth")
    Call<RespostaAutenticacao> login(@Body AutenticarSessao autenticarSessao);
}
