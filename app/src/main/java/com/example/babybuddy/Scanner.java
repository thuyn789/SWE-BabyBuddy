package com.example.babybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

public class Scanner extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference fire_reference;
    private String userID;

    private CodeScanner mCodeScanner;
    private String qrcode;
    private String fire_qrcode;

    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        back = (Button) findViewById(R.id.back_btn);
        back.setOnClickListener(this);
        
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
                        Toast.makeText(Scanner.this, "Scanned Successfully", Toast.LENGTH_SHORT).show();
                        qrCodeHandler();
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
            case R.id.back_btn:
                go_back();
                break;
        }
    }

    private void qrCodeHandler(){
        fire_reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    fire_qrcode = userProfile.qrcode;

                    if(fire_qrcode.compareTo("empty") == 0) {
                        //Place check in method here
                        fire_reference.child(userID).child("qrcode").setValue(qrcode);
                        Toast.makeText(Scanner.this, "Check In Successfully", Toast.LENGTH_LONG).show();
                    }else if(fire_qrcode.compareTo(qrcode) == 0){
                        //Place check out method here
                        fire_reference.child(userID).child("qrcode").setValue("empty");
                        Toast.makeText(Scanner.this, "Check Out Successfully", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(Scanner.this, "Please Try Again!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Scanner.this, "Failed to retrieve QR Code", Toast.LENGTH_LONG).show();
            }
        });

        go_back();
    }

    private void go_back() {
        startActivity(new Intent(Scanner.this, MainMenu.class));
        finish();
        return;
    }
}