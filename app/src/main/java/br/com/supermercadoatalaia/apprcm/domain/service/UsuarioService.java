package br.com.supermercadoatalaia.apprcm.domain.service;

import br.com.supermercadoatalaia.apprcm.domain.model.Usuario;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface UsuarioService {

    String END_POINT = "v1/usuarios";

    @GET(END_POINT+"/login/{login}")
    Call<Usuario> buscarPorLogin(@Header("Authorization") String token,
                                 @Path("login") String login);

}
