package br.com.supermercadoatalaia.apprcm.config;

import br.com.supermercadoatalaia.apprcm.core.ConfigApp;
import br.com.supermercadoatalaia.apprcm.domain.service.ColetaService;
import br.com.supermercadoatalaia.apprcm.domain.service.LoginService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitAtalaiaConfig {
    private final Retrofit retrofit;

    public RetrofitAtalaiaConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(ConfigApp.SERVER)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public LoginService getLoginService() {
        return this.retrofit.create(LoginService.class);
    }

    public ColetaService getColetaService() {
        return this.retrofit.create(ColetaService.class);
    }
}
