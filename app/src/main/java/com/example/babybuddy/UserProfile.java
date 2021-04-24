package com.example.babybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity implements View.OnClickListener{

    private FirebaseUser fire_user;
    private DatabaseReference fire_reference;
    private String userID;

    //Initalize variables
    private Button update;
    private EditText first_name_edittext, last_name_edittext, phone_number_edittext, email_edittext, password_edittext;
    private ProgressBar progress_bar;

    private String user_first_name, user_last_name, user_phone, user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        fire_user = FirebaseAuth.getInstance().getCurrentUser();
        fire_reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = fire_user.getUid();

        update = (Button) findViewById(R.id.update_btn);
        update.setOnClickListener(this);

        first_name_edittext = (EditText) findViewById(R.id.fname);
        last_name_edittext = (EditText) findViewById(R.id.lname);
        phone_number_edittext = (EditText) findViewById(R.id.phone);
        email_edittext = (EditText) findViewById(R.id.email);

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        //Fill out user data to EditText fields
        showUserData();
    }

    private void showUserData() {
        progress_bar.setVisibility(View.VISIBLE);
        fire_reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    user_first_name = userProfile.first_name;
                    user_last_name = userProfile.last_name;
                    user_phone = userProfile.phone_number;
                    user_email = userProfile.email;

                    first_name_edittext.setText(user_first_name);
                    last_name_edittext.setText(user_last_name);
                    phone_number_edittext.setText(user_phone);
                    email_edittext.setText(user_email);
                }
                progress_bar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "Failed to retrieve information", Toast.LENGTH_LONG).show();
                progress_bar.setVisibility(View.GONE);
            }
        });
    }

    private void updateUserData() {
        String first_name_inmethod = first_name_edittext.getText().toString().trim();
        String last_name_inmethod = last_name_edittext.getText().toString().trim();
        String phone_number_inmethod = phone_number_edittext.getText().toString().trim();
        String email_inmethod = email_edittext.getText().toString().trim();

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
        progress_bar.setVisibility(View.VISIBLE);

        if(first_name_inmethod.compareTo(user_first_name) != 0){
            fire_reference.child(userID).child("first_name").setValue(first_name_inmethod);
        }

        if(last_name_inmethod.compareTo(user_last_name) != 0){
            fire_reference.child(userID).child("last_name").setValue(last_name_inmethod);
        }

        if(phone_number_inmethod.compareTo(user_phone) != 0){
            fire_reference.child(userID).child("phone_number").setValue(phone_number_inmethod);
        }

        if(email_inmethod.compareTo(user_email) != 0){
            fire_reference.child(userID).child("email").setValue(email_inmethod);
        }

        progress_bar.setVisibility(View.GONE);

        Toast.makeText(UserProfile.this, "Update Successfully", Toast.LENGTH_LONG).show();

        startActivity(new Intent(UserProfile.this, MainMenu.class));
        finish();
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
            case R.id.update_btn:
                updateUserData();
                break;
        }
    }
}