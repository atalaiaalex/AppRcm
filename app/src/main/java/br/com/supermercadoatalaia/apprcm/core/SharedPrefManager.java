package br.com.supermercadoatalaia.apprcm.core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import br.com.supermercadoatalaia.apprcm.LoginActivity;
import br.com.supermercadoatalaia.apprcm.domain.model.Usuario;
import br.com.supermercadoatalaia.apprcm.dto.request.UserLogin;
import br.com.supermercadoatalaia.apprcm.dto.response.AuthenticationToken;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "shared_mem_rcm";
    private static final String KEY_ID = "id";
    private static final String KEY_NOME = "nome";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_TOKEN_EXPIRE = "token_expire";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TOKEN_FLEX = "token_flex";
    private static final String KEY_COOKIE_FLEX = "cookie_flex";
    private static final String KEY_LOGIN_FLEX = "login_flex";
    private static final String KEY_PASSWORD_FLEX = "password_flex";
    private static final String KEY_ATIVO = "ativo";
    private static final String KEY_GRUPOS = "grupos";
    private static final String KEY_ROLES = "roles";
    private static final String KEY_PERMISSOES = "permissoes";

    private static SharedPrefManager thisInstance;
    private static Context context;

    private SharedPrefManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (thisInstance == null) {
            thisInstance = new SharedPrefManager(context);
        }

        return thisInstance;
    }

    public void setUsuario(Usuario usuario) {
        SharedPreferences sharedPreferences = getSharedPreferences();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(KEY_ID, usuario.getId());
        editor.putString(KEY_NOME, usuario.getNome());
        editor.putString(KEY_USERNAME, usuario.getUsername());
        editor.putString(KEY_LOGIN_FLEX, usuario.getFlexLogin());
        editor.putBoolean(KEY_ATIVO, usuario.isAtivo());
        editor.putStringSet(KEY_GRUPOS, usuario.getGruposString());
        editor.putString(KEY_PASSWORD_FLEX, usuario.getFlexSenha());
        editor.putStringSet(KEY_PERMISSOES, usuario.getPermissoes());

        editor.apply();
    }

    public void userLoginFlex(Usuario usuario, String tokenFlex, String cookie) {
        SharedPreferences sharedPreferences = getSharedPreferences();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(KEY_ID, usuario.getId());
        editor.putString(KEY_NOME, usuario.getNome());
        editor.putString(KEY_USERNAME, usuario.getUsername());
        editor.putString(KEY_TOKEN_FLEX, tokenFlex);
        editor.putString(KEY_COOKIE_FLEX, cookie);
        editor.putString(KEY_LOGIN_FLEX, usuario.getFlexLogin());
        editor.putString(KEY_PASSWORD_FLEX, usuario.getFlexSenha());
        editor.putStringSet(KEY_PERMISSOES, usuario.getPermissoes());

        editor.apply();
    }

    public void userLogin(AuthenticationToken auth) {
        SharedPreferences sharedPreferences = getSharedPreferences();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_TOKEN, auth.getToken());
        editor.putString(KEY_USERNAME, auth.getUsername());
        editor.putLong(KEY_TOKEN_EXPIRE, auth.getExpire().getTime());
        editor.putStringSet(KEY_ROLES, auth.getRoles());

        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        return getSharedPreferences().getString(KEY_USERNAME, null) != null &&
                getSharedPreferences().getLong(KEY_TOKEN_EXPIRE, 0L) > new Date().getTime();
    }

    //this method will give the logged in user
    public Usuario getUsuario() {
        SharedPreferences sharedPreferences = getSharedPreferences();

        return new Usuario(
                sharedPreferences.getLong(KEY_ID, -1L),
                sharedPreferences.getString(KEY_NOME, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_LOGIN_FLEX, ""),
                sharedPreferences.getString(KEY_PASSWORD_FLEX, ""),
                sharedPreferences.getBoolean(KEY_ATIVO, false),
                new ArrayList<>(),
                sharedPreferences.getStringSet(KEY_PERMISSOES, Collections.emptySet())
        );
    }

    public UserLogin getUserLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences();

        return new UserLogin(
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_PASSWORD, null)
        );
    }

    public String getUserNome() {
        return getSharedPreferences().getString(KEY_USERNAME, null);
    }

    public Set<String> getUserRoles() {
        return getSharedPreferences().getStringSet(KEY_ROLES, Collections.emptySet());
    }

    public Set<String> getUsuarioPermissoes() {
        return getSharedPreferences().getStringSet(KEY_PERMISSOES, Collections.emptySet());
    }

    public String getAuthorizationBasic() {
        if(!isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        UserLogin user = getUserLogin();

        return "Basic " + Base64.encodeToString((user.getUsername() + ":" + user.getPassword())
                .getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP);
    }

    public String getAuthorizationToken() {
        if(!isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return "";
        }

        return "Bearer " + getSharedPreferences().getString(KEY_TOKEN, null);
    }

    public String getTokenFlex() {
        String tokenSalvo = getSharedPreferences().getString(KEY_TOKEN_FLEX, "");
        //Log.i("Token salvo", tokenSalvo);
        return tokenSalvo;
    }

    public String getCookieFlex() {
        String cookieSalvo = getSharedPreferences().getString(KEY_COOKIE_FLEX, "");
        //Log.i("Cookie salvo", cookieSalvo);
        return cookieSalvo;
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();

        context.startActivity(new Intent(context, LoginActivity.class));
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }
}
