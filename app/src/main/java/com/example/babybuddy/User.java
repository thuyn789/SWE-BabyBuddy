package com.example.babybuddy;

//This User class will be used as to create user objects to send to Firebase database
//and to retrieve users from Firebase database

public class User {
    //Initialize public variables
    public String first_name, last_name, phone_number, email, qrcode;

    //Initialize empty constructor
    public User(){}

    //Initialize constructor
    public User(String first_name, String last_name, String phone_number, String email){
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.email = email;
    }

    public String getPhone() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public String getQrcode() {
        return qrcode;
    }
    
    //The QR Code is not meant to be initialized by the constructor but by this method
    public void setQrcode(String qrcode){
        this.qrcode = qrcode;
    }
}
