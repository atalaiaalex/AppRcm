package br.com.supermercadoatalaia.apprcm.config;

import br.com.supermercadoatalaia.apprcm.domain.service.LoginFlexService;
import br.com.supermercadoatalaia.apprcm.domain.service.RCMFlexService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitFlexConfig {

    private final Retrofit retrofit;

    public RetrofitFlexConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.239:9000/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public LoginFlexService getLoginService() {
        return this.retrofit.create(LoginFlexService.class);
    }

    public RCMFlexService getRCMFlexService() {
        return this.retrofit.create(RCMFlexService.class);
    }
}
