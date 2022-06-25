package br.com.supermercadoatalaia.apprcm.domain.service;

import br.com.supermercadoatalaia.apprcm.domain.model.Host;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HostService {

    String END_POINT = "host";

    @GET(END_POINT+"/{senha}")
    Call<Host> getHost(@Path("senha") String senha);
}
