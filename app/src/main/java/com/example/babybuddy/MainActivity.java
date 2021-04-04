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
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.google.zxing.Result;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 100;
    private String qrcode;

    private TextView sign_up;
    private TextView scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sign_up = (TextView) findViewById(R.id.sign_up);
        sign_up.setOnClickListener(this);

        scanner = (TextView) findViewById(R.id.login_btn);
        scanner.setOnClickListener(this);

        getPermissions();
    }

    //Check and ask user for permissions
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

    @Override
    protected void onResume() {
        super.onResume();
        //mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        //mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up:
                startActivity(new Intent(this, SignUp.class));
                break;
            case R.id.login_btn:
                startActivity(new Intent(this, Scanner.class));
                break;
        }
    }
}