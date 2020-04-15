package com.example.debtapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQrActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private String TAG = ScanQrActivity.class.getSimpleName();

    public static String QR_CODE_RESULT = "qrCodeResult";

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.i(TAG, "handleResult: " + rawResult.getText());
        if (!rawResult.getText().isEmpty()){
            Intent resultIntent = new Intent();
            resultIntent.putExtra(QR_CODE_RESULT, rawResult.getText());
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
