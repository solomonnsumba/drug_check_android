package org.solomon.pharmascan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScannerView extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView scannerView;
    private SharedPreferences sharedPreferences;
    private String MY_PREFERENCES;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";



    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        try{
            getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }catch (NullPointerException e){}
        scanQRCode();
    }


    void scanQRCode(){
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }


    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Intent intent = new Intent("data");
        intent.putExtra("qr_result", result.getText());
        intent.putExtra("qr_format", result.getBarcodeFormat().toString());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
