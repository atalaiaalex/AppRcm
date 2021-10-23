package br.com.supermercadoatalaia.apprcm.core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.Charset;

import br.com.supermercadoatalaia.apprcm.LoginActivity;
import br.com.supermercadoatalaia.apprcm.domain.model.UserLogin;
import br.com.supermercadoatalaia.apprcm.domain.model.Usuario;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "shared_mem_rcm";
    private static final String KEY_ID = "id";
    private static final String KEY_NOME = "nome";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TOKEN_FLEX = "token_flex";
    private static final String KEY_COOKIE_FLEX = "cookie_flex";
    private static final String KEY_LOGIN_FLEX = "login_flex";
    private static final String KEY_PASSWORD_FLEX = "password_flex";

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

    //method to let the usuario login
    //this method will store the usuario data in shared preferences
    public void userLogin(Usuario usuario, String tokenFlex, String cookie) {
        SharedPreferences sharedPreferences = getSharedPreferences();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(KEY_ID, usuario.getId());
        editor.putString(KEY_NOME, usuario.getNome());
        editor.putString(KEY_USERNAME, usuario.getUsername());
        editor.putString(KEY_PASSWORD, usuario.getPassword());
        editor.putString(KEY_TOKEN_FLEX, tokenFlex);
        editor.putString(KEY_COOKIE_FLEX, cookie);
        editor.putString(KEY_LOGIN_FLEX, usuario.getFlexLogin());
        editor.putString(KEY_PASSWORD_FLEX, usuario.getFlexSenha());

        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        return getSharedPreferences().getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public Usuario getUsuario() {
        SharedPreferences sharedPreferences = getSharedPreferences();

        return new Usuario(
                sharedPreferences.getLong(KEY_ID, -1L),
                sharedPreferences.getString(KEY_NOME, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_PASSWORD, null),
                sharedPreferences.getString(KEY_LOGIN_FLEX, ""),
                sharedPreferences.getString(KEY_PASSWORD_FLEX, "")
        );
    }

    public UserLogin getUserLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences();

        return new UserLogin(
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_PASSWORD, null)
        );
    }

    public String getAuthorization() {
        if(!isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        UserLogin user = getUserLogin();

        return "Basic " + Base64.encodeToString((user.getUsername() + ":" + user.getPassword())
                .getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP);
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
