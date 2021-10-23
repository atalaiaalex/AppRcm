package br.com.supermercadoatalaia.apprcm.controller;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;

import br.com.supermercadoatalaia.apprcm.config.RetrofitAtalaiaConfig;
import br.com.supermercadoatalaia.apprcm.config.RetrofitFlexConfig;
import br.com.supermercadoatalaia.apprcm.core.SharedPrefManager;
import br.com.supermercadoatalaia.apprcm.domain.model.AutenticacaoResponse;
import br.com.supermercadoatalaia.apprcm.domain.model.AutenticarSessao;
import br.com.supermercadoatalaia.apprcm.domain.model.UserLogin;
import br.com.supermercadoatalaia.apprcm.domain.model.Usuario;
import br.com.supermercadoatalaia.apprcm.domain.service.LoginFlexService;
import br.com.supermercadoatalaia.apprcm.domain.service.LoginService;
import br.com.supermercadoatalaia.apprcm.exception.RegistroNotFoundException;
import retrofit2.Call;
import retrofit2.Response;

public class LoginController {

    private final LoginService service;
    private final LoginFlexService flexService;
    private Context context;

    public LoginController(Context context) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.service = new RetrofitAtalaiaConfig().getLoginService();
        this.flexService = new RetrofitFlexConfig().getLoginService();
        this.context = context;
    }

    public Usuario login(UserLogin user) throws RegistroNotFoundException {
        Call<Usuario> call = service.login(user);

        Response<Usuario> usuario = null;
        try {
            usuario = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar usuario", e);
        }

        if(usuario.isSuccessful()) {
            Usuario usuarioResponse = usuario.body();
            usuarioResponse.setPassword(user.getPassword());

            logarFlex(usuarioResponse);

            return usuarioResponse;
        }

        throw new RegistroNotFoundException("Usuario não encontrado");
    }

    private void logarFlex(Usuario usuario) throws RegistroNotFoundException {
        AutenticarSessao autenticarSessao = new AutenticarSessao(
                usuario.getFlexLogin(),
                usuario.getFlexSenha()
        );

        Call<AutenticacaoResponse> call = flexService.login(autenticarSessao);

        Response<AutenticacaoResponse> usuarioFlex = null;
        try {
            usuarioFlex = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar usuario do Flex", e);
        }

        if(usuarioFlex.isSuccessful()) {
            SharedPrefManager.getInstance(context).userLogin(
                    usuario,
                    usuarioFlex.body().getResponse().getToken(),
                    usuarioFlex.headers().get("Set-Cookie")
            );
        }
    }

    public void logout() {
        Call<Void> call = service.logout(SharedPrefManager.getInstance(context).getAuthorization());
        Call<Void> callFlex = flexService.logout(
                SharedPrefManager.getInstance(context).getTokenFlex(),
                SharedPrefManager.getInstance(context).getCookieFlex()
        );

        try {
            call.execute();
            callFlex.execute();
        } catch (IOException e) {
            Log.e("Logout", e.getMessage());
        } finally {
            SharedPrefManager.getInstance(context).logout();
        }
    }
}
