package com.example.babybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private TextView scanner_in;
    private TextView scanner_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        scanner_in = (TextView) findViewById(R.id.checkin_btn);
        scanner_in.setOnClickListener(this);

        scanner_out = (TextView) findViewById(R.id.checkout_btn);
        scanner_out.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkin_btn:
                startActivity(new Intent(this, Scanner.class));
                break;
            case R.id.checkout_btn:
                startActivity(new Intent(this, Scanner.class));
                break;
        }
    }
}