package com.example.babybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private Button scanner_in, scanner_out, timer, logout;

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
            case R.id.logout_btn:
                startActivity(new Intent(MainMenu.this, MainActivity.class));
                break;
        }
    }
}