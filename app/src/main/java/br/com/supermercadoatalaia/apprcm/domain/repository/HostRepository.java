package br.com.supermercadoatalaia.apprcm.domain.repository;

import android.content.Context;

import java.io.IOException;

import br.com.supermercadoatalaia.apprcm.config.RetrofitHostConfig;
import br.com.supermercadoatalaia.apprcm.domain.model.Host;
import br.com.supermercadoatalaia.apprcm.domain.service.HostService;
import br.com.supermercadoatalaia.apprcm.exception.RegistroNotFoundException;
import retrofit2.Call;
import retrofit2.Response;

public class HostRepository {

    private Context context;
    private final HostService service;

    public HostRepository (Context context) {
        this.service = new RetrofitHostConfig().getHostService();
        this.context = context;
    }

    public Host getHost(String senha) throws RegistroNotFoundException {

        Call<Host> call = service.getHost(senha);

        Response<Host> host;
        try {
            host = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar host", e);
        }

        if(host.isSuccessful()) {
            return host.body();
        }

        throw new RegistroNotFoundException("Host não encontrado");
    }
}
