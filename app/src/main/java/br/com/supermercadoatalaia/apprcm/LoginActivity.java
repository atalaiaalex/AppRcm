package br.com.supermercadoatalaia.apprcm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import br.com.supermercadoatalaia.apprcm.controller.LoginController;
import br.com.supermercadoatalaia.apprcm.core.ApiConsumer;
import br.com.supermercadoatalaia.apprcm.core.ConfigApp;
import br.com.supermercadoatalaia.apprcm.core.SharedPrefManager;
import br.com.supermercadoatalaia.apprcm.domain.model.UserLogin;
import br.com.supermercadoatalaia.apprcm.domain.model.Usuario;
import br.com.supermercadoatalaia.apprcm.exception.RegistroNotFoundException;

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

            try {
                Usuario usuario = new LoginController(getApplicationContext()).login(user);

                Toast.makeText(
                        getApplicationContext(),
                        "Bem vindo " + usuario.getNome(),
                        Toast.LENGTH_LONG
                ).show();

                startActivity(new Intent(this, MainActivity.class));
            }catch (RegistroNotFoundException e) {
                Toast.makeText(
                        getApplicationContext(),
                        "Erro ao logar" + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
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
