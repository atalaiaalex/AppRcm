package br.com.supermercadoatalaia.apprcm.domain.service;

import br.com.supermercadoatalaia.apprcm.domain.model.UserLogin;
import br.com.supermercadoatalaia.apprcm.domain.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginService {

    @POST("login")
    Call<Usuario> login(@Body UserLogin userLogin);

    @POST("logout")
    Call<Void> logout(@Header("Authorization") String auth);
}
