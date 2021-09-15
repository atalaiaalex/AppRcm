package br.com.supermercadoatalaia.apprcm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import br.com.supermercadoatalaia.apprcm.config.RetrofitAtalaiaConfig;
import br.com.supermercadoatalaia.apprcm.config.RetrofitFlexConfig;
import br.com.supermercadoatalaia.apprcm.core.ApiConsumer;
import br.com.supermercadoatalaia.apprcm.core.ConfigApp;
import br.com.supermercadoatalaia.apprcm.core.SharedPrefManager;
import br.com.supermercadoatalaia.apprcm.domain.model.AutenticarSessao;
import br.com.supermercadoatalaia.apprcm.domain.model.RespostaAutenticacao;
import br.com.supermercadoatalaia.apprcm.domain.model.RespostaRCMInserir;
import br.com.supermercadoatalaia.apprcm.domain.model.UserLogin;
import br.com.supermercadoatalaia.apprcm.domain.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUser;
    private EditText txtPassword;
    private Button btnLogin;

    private ConfigApp configApp;

    private static final int PERMISSAO_IO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponent();
        initPermissoes();
        initConfigApp();

        try {
            ApiConsumer.SERVER = configApp.lerTxt();

            if(SharedPrefManager.getInstance(this).isLoggedIn()) {
                finish();
                startActivity(new Intent(this, MainActivity.class));
            }
        } catch (IOException e) {
            configurar();
        }
    }

    private View.OnClickListener btnLogin_Click() {
        return v -> {
            ApiConsumer apiConsumer = new ApiConsumer();

            final String username = txtUser.getText().toString();
            final String password = txtPassword.getText().toString();

            if (TextUtils.isEmpty(username)) {
                txtUser.setError("Por favor insira seu login");
                txtUser.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                txtPassword.setError("Por favor insira sua senha");
                txtPassword.requestFocus();
                return;
            }

            UserLogin user = new UserLogin(username, password);

            Call<Usuario> call = new RetrofitAtalaiaConfig().getLoginService().login(user);

            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                    Usuario user = response.body();
                    user.setPassword(password);

                    Call<RespostaAutenticacao> callFlex = new RetrofitFlexConfig()
                            .getLoginService().login(
                                    new AutenticarSessao("100000", "272108")
                            );

                    Log.i("LoginFlexService", "Iniciando Login");

                    callFlex.enqueue(new Callback<RespostaAutenticacao>() {
                        @Override
                        public void onResponse(Call<RespostaAutenticacao> call,
                                               Response<RespostaAutenticacao> response) {

                            Log.i("LoginFlexService", "Logado, instanciando resposta");

                            RespostaAutenticacao respostaAutenticacao = response.body();
                            String token = respostaAutenticacao.getResponse().getToken();

                            Log.i("LoginFlexService", "Token obtido \n\t\t" + token);

                            SharedPrefManager.getInstance(getApplicationContext())
                                    .userLogin(user, token);

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }

                        @Override
                        public void onFailure(Call<RespostaAutenticacao> call, Throwable t) {
                            Log.e("LoginFlexService   ", "Erro ao efetuar login :: " + t.getMessage());
                        }
                    });
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Log.e("LoginService   ", "Erro ao efetuar login :: " + t.getMessage());
                }
            });
        };
    }

    private void initComponent() {
        txtUser = findViewById(R.id.txtUser);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(btnLogin_Click());
    }

    private void configurar() {
        CaixaDialogoHost dialogo = new CaixaDialogoHost(
                "Path Server Host",
                configApp
        );

        dialogo.show(getSupportFragmentManager(), "DialogoHostApi");
    }

    private void initConfigApp() {
        configApp = new ConfigApp(
                getExternalFilesDir(ConfigApp.PASTA_CONFIG).getAbsolutePath()
        );
    }

    private void initPermissoes () {
        //USUARIO DAR A PERMISSAO PARA LER
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_IO);
            }
        }

        //USUARIO DAR A PERMISSAO PARA ESCREVER
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSAO_IO);
            }
        }
    }
}
