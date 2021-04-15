package com.example.babybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.Result;

public class Scanner extends AppCompatActivity implements View.OnClickListener {

    private CodeScanner mCodeScanner;
    private String qrcode;

    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        logout = (Button) findViewById(R.id.logout_btn);
        logout.setOnClickListener(this);
        
        codeScanner();
    }

    private void codeScanner() {

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);

        //Decoding
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        qrcode = result.getText();
                        Toast.makeText(Scanner.this, qrcode, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //When camera fail to initialize/scan
        mCodeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@NonNull Exception error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Scanner.this, "Camera Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //Tap on scanner view to preview before camera begin scanning
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout_btn:
                user_logout();
                break;
        }
    }

    private void user_logout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(Scanner.this, MainActivity.class));
        return;
    }
}