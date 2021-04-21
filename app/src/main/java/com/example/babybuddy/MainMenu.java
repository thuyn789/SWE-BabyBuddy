package com.example.babybuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private Button scanner_in, scanner_out, timer, logout, ec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        scanner_in = (Button) findViewById(R.id.checkin_btn);
        scanner_in.setOnClickListener(this);

        scanner_out = (Button) findViewById(R.id.checkout_btn);
        scanner_out.setOnClickListener(this);

        timer = (Button) findViewById(R.id.settime_btn);
        timer.setOnClickListener(this);

        logout = (Button) findViewById(R.id.logout_btn);
        logout.setOnClickListener(this);

        ec = (Button) findViewById(R.id.EC_btn);
        ec.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user_logout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkin_btn:
                startActivity(new Intent(MainMenu.this, Scanner.class));
                break;
            case R.id.checkout_btn:
                startActivity(new Intent(MainMenu.this, Scanner.class));
                break;
            case R.id.settime_btn:
                startActivity(new Intent(MainMenu.this, SetTimer.class));
                break;
            case R.id.EC_btn:
                startActivity(new Intent(MainMenu.this, EmergencyContactInfo.class));
                break;
            case R.id.logout_btn:
                user_logout();
                break;
        }
    }

    private void user_logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainMenu.this, MainActivity.class));
        finish();
        return;
    }
}