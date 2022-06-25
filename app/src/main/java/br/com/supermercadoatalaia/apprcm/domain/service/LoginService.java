package br.com.supermercadoatalaia.apprcm.domain.service;

import br.com.supermercadoatalaia.apprcm.dto.request.UserLogin;
import br.com.supermercadoatalaia.apprcm.dto.response.AuthenticationToken;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginService {

    @POST("login")
    Call<AuthenticationToken> login(@Body UserLogin userLogin);

    @GET("logout")
    Call<Void> logout();
}
