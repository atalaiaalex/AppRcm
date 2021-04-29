package br.com.supermercadoatalaia.apprcm;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

public class ScannerActivity extends Activity implements
        CompoundBarcodeView.TorchListener {

    private boolean isFlashOn;

    private CaptureManager capture;
    private CompoundBarcodeView barcodeScannerView;
    private ImageButton btnSairCamera;
    private ImageButton btnFlash;

    private static final int PERMISSAO_CAMERA = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        //https://github.com/SunhoY/zxing-android-embedded

        initPermissoes();
        initComponentes(savedInstanceState);
    }

    private void initPermissoes () {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSAO_CAMERA
        );
    }

    private void initComponentes(Bundle savedInstanceState) {
        btnSairCamera = findViewById(R.id.btnSairCamera);
        btnFlash = findViewById(R.id.btnFlash);

        btnSairCamera.setOnClickListener(btnSairCameraOnClick());
        btnFlash.setOnClickListener(btnFlashOnClick());

        if (!hasFlash()) {
            btnFlash.setVisibility(View.GONE);
        }

        barcodeScannerView = (CompoundBarcodeView)findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private void flashCamera(){
        if(isFlashOn){
            barcodeScannerView.setTorchOff();
        }else {
            barcodeScannerView.setTorchOn();
        }
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
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTorchOn() {
        isFlashOn = true;
        Toast.makeText(this, "Flash ligado", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTorchOff() {
        isFlashOn = false;
        Toast.makeText(this, "Flash desligado", Toast.LENGTH_LONG).show();
    }
}
