package com.example.babybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private static final int REQUEST_CODE = 100;

    private TextView sign_up;
    private Button login;
    private ProgressBar progress_bar;

    private EditText email_edittext, password_edittext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        sign_up = (TextView) findViewById(R.id.sign_up);
        sign_up.setOnClickListener(this);

        login = (Button) findViewById(R.id.login_btn);
        login.setOnClickListener(this);

        email_edittext = (EditText) findViewById(R.id.email);
        password_edittext = (EditText) findViewById(R.id.password);

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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
            case R.id.sign_up:
                startActivity(new Intent(MainActivity.this, SignUp.class));
                break;
            case R.id.login_btn:
                user_login();
                break;
        }
    }

    private void user_login() {
        String email_inmethod = email_edittext.getText().toString().trim();
        String password_inmethod = password_edittext.getText().toString().trim();

        //Check if user input email address or not
        if(email_inmethod.isEmpty()){
            email_edittext.setError("Please enter email address");
            email_edittext.requestFocus();
            return;
        }else{
            //Validate if user input email in a correct format
            if(!Patterns.EMAIL_ADDRESS.matcher(email_inmethod).matches()) {
                email_edittext.setError("Your email is not valid");
                email_edittext.requestFocus();
                return;
            }
        }

        //Check if user input password or not
        if(password_inmethod.isEmpty()){
            password_edittext.setError("Please enter password");
            password_edittext.requestFocus();
            return;
        }

        progress_bar.setVisibility(View.VISIBLE);

        //Start the process of login user
        mAuth.signInWithEmailAndPassword(email_inmethod,password_inmethod).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progress_bar.setVisibility(View.GONE);
                    finish();
                    //Take user to main menu page;
                    startActivity(new Intent(MainActivity.this, MainMenu.class));
                }else{
                    //Failed to sign up new user
                    Toast.makeText(MainActivity.this,"Username/Password is incorrect!", Toast.LENGTH_LONG).show();
                    progress_bar.setVisibility(View.GONE);
                }
            }
        });
    }
    private void userIsLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), MainMenu.class));
            finish();
            return;
        }
    }
}