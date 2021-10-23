package br.com.supermercadoatalaia.apprcm.domain.service;

import br.com.supermercadoatalaia.apprcm.domain.model.RCMFlex;
import br.com.supermercadoatalaia.apprcm.domain.model.RespostaRCMInserir;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RCMFlexService {

    @POST("v1.1/recmerc/inserir")
    Call<RespostaRCMInserir> inserirRCM(@Header("token") String token,
                                        @Header("Cookie") String cookie,
                                        @Body RCMFlex rcm);

}
