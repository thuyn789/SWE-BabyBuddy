package com.example.babybuddy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.Result;

public class MainPageActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private static final int REQUEST_CODE = 100;
    private String qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button mLogout = findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() { //firebase functionality
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), com.example.babybuddy.LoginActivity.class); //page only for logged in user
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            }
        });

        getPermissions();
        codeScanner();
    }//end create

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
                        Toast.makeText(MainPageActivity.this, qrcode, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainPageActivity.this, "Camera Failed!", Toast.LENGTH_SHORT).show();
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
    }//codeScanner

    protected void onResume(){
        super.onResume();
        mCodeScanner.startPreview();
    }

    protected void onPause(){
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void getPermissions() {
        //Create string to hold all permission variable
        String[] permissions = {Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CAMERA};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[2]) == PackageManager.PERMISSION_GRANTED){
                return;
            }else {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
            }
        }
    }//end Permissions

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        getPermissions();
    }
}//end class
