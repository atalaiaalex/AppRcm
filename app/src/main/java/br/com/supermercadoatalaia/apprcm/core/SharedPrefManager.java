package br.com.supermercadoatalaia.apprcm.core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import br.com.supermercadoatalaia.apprcm.LoginActivity;
import br.com.supermercadoatalaia.apprcm.domain.model.Usuario;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_ID = "id";
    private static final String KEY_NOME = "nome";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ATIVO = "enabled";

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
    public void userLogin(Usuario usuario) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(KEY_ID, usuario.getId());
        editor.putString(KEY_NOME, usuario.getNome());
        editor.putString(KEY_LOGIN, usuario.getLogin());
        editor.putString(KEY_PASSWORD, usuario.getPassword());
        editor.putBoolean(KEY_ATIVO, usuario.isAtivo());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_LOGIN, null) != null;
    }

    //this method will give the logged in user
    public Usuario getUsuario() {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return new Usuario(
                sharedPreferences.getLong(KEY_ID, -1L),
                sharedPreferences.getString(KEY_NOME, null),
                sharedPreferences.getString(KEY_LOGIN, null),
                sharedPreferences.getString(KEY_PASSWORD, null),
                sharedPreferences.getBoolean(KEY_ATIVO, false)
        );
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
}
