package br.com.supermercadoatalaia.apprcm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import br.com.supermercadoatalaia.apprcm.core.ApiConsumer;
import br.com.supermercadoatalaia.apprcm.core.ConfigApp;
import br.com.supermercadoatalaia.apprcm.core.HttpResposta;

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

        //Log.i("Json da resposta", jsonReader.nextName());
    }

    private View.OnClickListener btnLogin_Click() {
        return v -> {
            try {
                ApiConsumer apiConsumer = new ApiConsumer();
                apiConsumer.carregarConfiguracao();
                apiConsumer.iniciarConexao("GET", new URL(ApiConsumer.LOGIN));
                ApiConsumer.token =
                    Base64.encodeToString((txtUser.getText().toString()+":"+txtPassword.getText().toString())
                        .getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);

                apiConsumer.processarComResposta();
                HttpResposta httpResposta = apiConsumer.getHttpResposta();

                apiConsumer.fecharConexao();

                if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Falha no login", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Falha na conexão", Toast.LENGTH_LONG).show();
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

        try {
            ApiConsumer.SERVER = configApp.lerTxt();
        } catch (IOException e) {
            configurar();
        }
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
