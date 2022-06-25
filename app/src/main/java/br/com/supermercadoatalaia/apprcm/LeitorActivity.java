package br.com.supermercadoatalaia.apprcm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Arrays;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class LeitorActivity extends AppCompatActivity
        implements ZXingScannerView.ResultHandler {

    private ZXingScannerView z_xing_scanner;
    private ImageButton btnSairCamera;
    private ImageButton btnFlash;

    private static final int PERMISSAO_CAMERA = 150;
    public static final String RETORNO_LEITURA = "leitura";
    public static final int SUCESSO = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitor);

        z_xing_scanner = findViewById(R.id.z_xing_scanner);
        btnSairCamera = findViewById(R.id.btnSairCamera);
        btnFlash = findViewById(R.id.btnFlash);

        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSAO_CAMERA
        );

        btnSairCamera.setOnClickListener(v -> {
            onPause();
            finish();
        });
        btnFlash.setOnClickListener(v -> flashCamera());
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
         * Registrando a entidade para que ela possa
         * trabalhar os resultados de scan. Seguindo a
         * documentação, o código entra no onResume().
         */
		 
        z_xing_scanner.setResultHandler(this);
        startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();

        z_xing_scanner.stopCamera();
    }

    private void startCamera(){
        /*
         * Sem nenhum parâmetro definido em startCamera(),
         * digo, o parâmetro idCamera, a câmera de ID 0
         * será a utilizada, ou seja, a câmera de tras
         * (rear-facing) do device. A câmera da frente
         * (front-facing) é a de ID 1.
         */

        z_xing_scanner.setFormats(Arrays.asList(BarcodeFormat.EAN_13));
        z_xing_scanner.setAutoFocus(true);
        z_xing_scanner.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        onPause();

        /* CÓDIGO DE PROCESSAMENTO DE SIMBOLOGIA LIDA */
        Intent data = new Intent();
        data.putExtra(RETORNO_LEITURA, result.getText());
        setResult(SUCESSO, data);
        finish();
    }

    private boolean flashCamera(){
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            z_xing_scanner.toggleFlash();
            return true;
        }

        return false;
    }
}