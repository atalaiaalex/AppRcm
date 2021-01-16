package br.com.supermercadoatalaia.apprcm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
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
import br.com.supermercadoatalaia.apprcm.core.SharedPrefManager;
import br.com.supermercadoatalaia.apprcm.core.exception.ApiException;
import br.com.supermercadoatalaia.apprcm.domain.model.Usuario;

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

            try {
                SharedPrefManager.getInstance(getApplicationContext())
                        .userLogin(
                                new Usuario(
                                        0L,
                                        "",
                                        username,
                                        password,
                                        true)
                );

                apiConsumer.iniciarConexao(
                        "POST",
                        new URL(ApiConsumer.USUARIO),
                        getApplicationContext()
                );
                apiConsumer.addCabecalho("Content-Type", "application/json");
                apiConsumer.addCabecalho("Accept", "application/json");

                setUsuario(
                        apiConsumer.getJsonWriter(),
                        new Usuario(0L, "", username, "", false)
                );

                JsonReader jsonReader = apiConsumer.getJsonReader();
                HttpResposta httpResposta = apiConsumer.getHttpResposta();

                if(httpResposta.getCode() >= 200 && httpResposta.getCode() < 300) {
                    SharedPrefManager.getInstance(getApplicationContext())
                            .userLogin(instanciarUsuario(jsonReader));
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            new ApiException(httpResposta, jsonReader).getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }

                apiConsumer.fecharConexao();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Falha na conexão", Toast.LENGTH_LONG).show();
            }
        };
    }

    private void setUsuario(JsonWriter jsonWriter, Usuario usuario) throws IOException {
        jsonWriter.setIndent("  ");
        jsonWriter.beginObject();
        jsonWriter.name("id").value(usuario.getId());
        jsonWriter.name("nome").value(usuario.getNome());
        jsonWriter.name("login").value(usuario.getLogin());
        jsonWriter.name("senha").value(usuario.getPassword());
        jsonWriter.name("ativo").value(usuario.isAtivo());
        jsonWriter.endObject();
        jsonWriter.close();
    }

    private Usuario instanciarUsuario(JsonReader jsonReader) throws IOException {
        Long id = 0L;
        String nome = "";
        String login = "";
        String password = "";
        boolean ativo = false;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String key = jsonReader.nextName();
            if (key.equals("id")) {
                id = jsonReader.nextLong();
            } else if(key.equals("nome") && jsonReader.peek() != JsonToken.NULL) {
                nome = jsonReader.nextString();
            } else if(key.equals("username") && jsonReader.peek() != JsonToken.NULL) {
                login = jsonReader.nextString();
            } else if(key.equals("password") && jsonReader.peek() != JsonToken.NULL) {
                password = txtPassword.getText().toString();
                jsonReader.skipValue();
            } else if(key.equals("enabled") && jsonReader.peek() != JsonToken.NULL) {
                ativo = jsonReader.nextBoolean();
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        jsonReader.close();

        return new Usuario(
                id,
                nome,
                login,
                password,
                ativo
        );
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
