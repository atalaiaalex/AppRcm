package br.com.supermercadoatalaia.apprcm.config;

import br.com.supermercadoatalaia.apprcm.domain.service.HostService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitHostConfig {

    private final Retrofit retrofit;

    public RetrofitHostConfig() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ConfigApp.SERVER_HOST)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public HostService getHostService() {
        return retrofit.create(HostService.class);
    }
}
