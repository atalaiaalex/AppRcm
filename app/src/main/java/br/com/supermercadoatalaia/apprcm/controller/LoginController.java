package br.com.supermercadoatalaia.apprcm.controller;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;

import br.com.supermercadoatalaia.apprcm.config.RetrofitAtalaiaConfig;
import br.com.supermercadoatalaia.apprcm.config.RetrofitFlexConfig;
import br.com.supermercadoatalaia.apprcm.core.SharedPrefManager;
import br.com.supermercadoatalaia.apprcm.domain.model.Usuario;
import br.com.supermercadoatalaia.apprcm.domain.service.LoginFlexService;
import br.com.supermercadoatalaia.apprcm.domain.service.LoginService;
import br.com.supermercadoatalaia.apprcm.domain.service.UsuarioService;
import br.com.supermercadoatalaia.apprcm.dto.request.AutenticarSessao;
import br.com.supermercadoatalaia.apprcm.dto.request.UserLogin;
import br.com.supermercadoatalaia.apprcm.dto.response.AutenticacaoResponse;
import br.com.supermercadoatalaia.apprcm.dto.response.AuthenticationToken;
import br.com.supermercadoatalaia.apprcm.exception.RegistroNotFoundException;
import retrofit2.Call;
import retrofit2.Response;

public class LoginController {

    private final LoginService service;
    private final LoginFlexService flexService;
    private final UsuarioService usuarioService;
    private Context context;

    public LoginController(Context context) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.service = new RetrofitAtalaiaConfig().getLoginService();
        this.flexService = new RetrofitFlexConfig().getLoginService();
        this.usuarioService = new RetrofitAtalaiaConfig().getUsuarioService();
        this.context = context;
    }

    public void login(UserLogin user) throws RegistroNotFoundException {
        Call<AuthenticationToken> call = service.login(user);

        Response<AuthenticationToken> usuario;
        try {
            usuario = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Usuário ou senha inválida", e);
        }

        if(usuario.isSuccessful()) {
            AuthenticationToken auth = usuario.body();

            SharedPrefManager.getInstance(context).userLogin(auth);

            this.setUsuario();
        }else {
            throw new RegistroNotFoundException("Usuário ou senha inválida");
        }
    }

    private void setUsuario() throws RegistroNotFoundException {
        Call<Usuario> call = usuarioService.buscarPorLogin(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                SharedPrefManager.getInstance(context).getUserNome()
        );

        Response<Usuario> usuario;
        try {
            usuario = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao obter dados do usuário", e);
        }

        if(usuario.isSuccessful()) {
            SharedPrefManager.getInstance(context).setUsuario(usuario.body());
        }else {
            throw new RegistroNotFoundException("Erro ao obter dados do usuário");
        }
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
            SharedPrefManager.getInstance(context).userLoginFlex(
                    usuario,
                    usuarioFlex.body().getResponse().getToken(),
                    usuarioFlex.headers().get("Set-Cookie")
            );
        }
    }

    public void logout() {
        SharedPrefManager.getInstance(context).logout();

        Call<Void> call = service.logout();
        Call<Void> callFlex = flexService.logout(
                SharedPrefManager.getInstance(context).getTokenFlex(),
                SharedPrefManager.getInstance(context).getCookieFlex()
        );

        try {
            call.execute();
            callFlex.execute();
        } catch (IOException e) {
            Log.e("Logout", e.getMessage());
        }
    }
}
