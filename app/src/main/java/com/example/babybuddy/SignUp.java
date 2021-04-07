package com.example.babybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    //Initalize variables
    private TextView banner;
    private Button sign_up;
    private EditText first_name_edittext, last_name_edittext, phone_number_edittext, email_edittext, password_edittext;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        sign_up = (Button) findViewById(R.id.sign_up_btn);
        sign_up.setOnClickListener(this);

        first_name_edittext = (EditText) findViewById(R.id.fname);
        last_name_edittext = (EditText) findViewById(R.id.lname);
        phone_number_edittext = (EditText) findViewById(R.id.phone);
        email_edittext = (EditText) findViewById(R.id.email);
        password_edittext = (EditText) findViewById(R.id.password);

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up_btn:
                sign_up();
                break;
        }
    }

    private void sign_up() {
        String first_name_inmethod = first_name_edittext.getText().toString().trim();
        String last_name_inmethod = last_name_edittext.getText().toString().trim();
        String phone_number_inmethod = phone_number_edittext.getText().toString().trim();
        String email_inmethod = email_edittext.getText().toString().trim();
        String password_inmethod = password_edittext.getText().toString().trim();

        //Check if user input first name or not
        if(first_name_inmethod.isEmpty()){
            first_name_edittext.setError("Please enter first name");
            first_name_edittext.requestFocus();
            return;
        }

        //Check if user input last name or not
        if(last_name_inmethod.isEmpty()){
            last_name_edittext.setError("Please enter last name");
            last_name_edittext.requestFocus();
            return;
        }

        //Check if user input phone number or not
        if(phone_number_inmethod.isEmpty()){
            phone_number_edittext.setError("Please enter phone number");
            phone_number_edittext.requestFocus();
            return;
        }else{
            //Validate if user input phone number in a correct format
            if(!Patterns.PHONE.matcher(phone_number_inmethod).matches()) {
                phone_number_edittext.setError("Your phone number is not valid");
                phone_number_edittext.requestFocus();
                return;
            }
        }

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

        //Start the process of creating new user with new password
        mAuth.createUserWithEmailAndPassword(email_inmethod,password_inmethod)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(first_name_inmethod, last_name_inmethod, phone_number_inmethod,email_inmethod);

                    //Calling Firebase Database object
                    //Adding new "user" object to Firebase database
                    //Calling addOnCompleteListener to show the result of the adding object process
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                //addOnCompleteListener will show the result of the process here
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUp.this,"Signed Up Successfully", Toast.LENGTH_LONG).show();
                                progress_bar.setVisibility(View.GONE);

                                //Take user back to login page;
                                startActivity(new Intent(SignUp.this, MainActivity.class));
                            }else{
                                //Failed to sign up new user
                                Toast.makeText(SignUp.this,"Signed Up Failed", Toast.LENGTH_LONG).show();
                                progress_bar.setVisibility(View.GONE);

                                //Take user back to login page;
                                startActivity(new Intent(SignUp.this, MainActivity.class));
                            }
                        }
                    });
                }else{
                    //Failed to sign up new user
                    Toast.makeText(SignUp.this,"Signed Up Failed", Toast.LENGTH_LONG).show();
                    progress_bar.setVisibility(View.GONE);
                }
            }
        });
    }
}