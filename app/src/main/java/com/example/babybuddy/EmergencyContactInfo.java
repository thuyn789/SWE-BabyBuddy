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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmergencyContactInfo extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase DB = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();

    //Initialize variables
    private Button submit;
    private EditText fname_edittext, lname_edittext, phone_edittext, email_edittext, relation_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);


        submit = (Button) findViewById(R.id.submit_btn);
        submit.setOnClickListener(this);

        fname_edittext = (EditText) findViewById(R.id.fname_ec);
        lname_edittext = (EditText) findViewById(R.id.lname_ec);
        phone_edittext = (EditText) findViewById(R.id.phone_ec);
        email_edittext = (EditText) findViewById(R.id.email_ec);
        relation_edittext = (EditText) findViewById(R.id.relationship);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_btn:
                submit_ec();
                break;
        }
    }

    private void submit_ec() {

        String fname_entry = fname_edittext.getText().toString().trim();
        String lname_entry = lname_edittext.getText().toString().trim();
        String phone_entry = phone_edittext.getText().toString().trim();
        String email_entry = email_edittext.getText().toString().trim();
        String relation_entry = relation_edittext.getText().toString().trim();

        //Check if user input first name or not
        if(fname_entry.isEmpty()){
            fname_edittext.setError("Please enter first name");
            fname_edittext.requestFocus();
            return;
        }

        //Check if user input last name or not
        if(lname_entry.isEmpty()){
            lname_edittext.setError("Please enter last name");
            lname_edittext.requestFocus();
            return;
        }

        //Check if user input phone number or not
        if(phone_entry.isEmpty()){
            phone_edittext.setError("Please enter phone number");
            phone_edittext.requestFocus();
            return;
        }else{
            //Validate if user input phone number in a correct format
            if(!Patterns.PHONE.matcher(phone_entry).matches()) {
                phone_edittext.setError("Your phone number is not valid");
                phone_edittext.requestFocus();
                return;
            }
        }

        //Check if user input email address or not
        if(email_entry.isEmpty()){
            email_edittext.setError("Please enter email address");
            email_edittext.requestFocus();
            return;
        }else{
            //Validate if user input email in a correct format
            if(!Patterns.EMAIL_ADDRESS.matcher(email_entry).matches()) {
                email_edittext.setError("Your email is not valid");
                email_edittext.requestFocus();
                return;
            }
        }

        //Check if user input relationship or not
        if(relation_entry.isEmpty()){
            relation_edittext.setError("Please enter relationship");
            relation_edittext.requestFocus();
            return;
        }

        //Declaring path to Current User's Emergency Contact Information
        DatabaseReference ContactInfo = DB.getReference("Users/" + userID
                + "/Emergency Contact Information: ");

        EmergencyContact EC = new EmergencyContact(fname_entry, lname_entry,
                phone_entry, email_entry, relation_entry);

        ContactInfo.push().setValue(EC);
    }

}
