package br.com.supermercadoatalaia.apprcm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class LeitorActivity extends AppCompatActivity
        implements ZXingScannerView.ResultHandler {

    private ZXingScannerView z_xing_scanner;
    private ImageButton btnSairCamera;
    private ImageButton btnFlash;

    private static final int PERMISSAO_CAMERA = 150;
    public static final String LEITURA = "leitura";
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

        btnSairCamera.setOnClickListener(btnSairCameraOnClick());
        btnFlash.setOnClickListener(btnFlashOnClick());
    }

    private View.OnClickListener btnSairCameraOnClick () {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };
    }

    private View.OnClickListener btnFlashOnClick () {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashCamera();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
         * Registrando a entidade para que ela possa
         * trabalhar os resultados de scan. Seguindo a
         * documentação, o código entra no onResume().
         * */
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

        z_xing_scanner.setAutoFocus(true);
        z_xing_scanner.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        //Log.i("LOG", "Conteudo do código lido: "+result.getText());
        //Log.i("LOG", "Formato do código lido: "+result.getBarcodeFormat().name());
        z_xing_scanner.resumeCameraPreview( this );
        onPause();

        /* CÓDIGO DE PROCESSAMENTO DE SIMBOLOGIA LIDA */
        Intent data = new Intent();
        data.putExtra(LEITURA, result.getText());
        setResult(SUCESSO, data);
        finish();
    }

    private boolean flashCamera(){
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            z_xing_scanner.toggleFlash();
            //z_xing_scanner.setDefaultFocusHighlightEnabled(true); //Só vale para API alta...
            return true;
        }

        return false;
    }

}