package com.example.babybuddy;

//This User class will be used as to create user objects to send to Firebase database
//and to retrieve users from Firebase database

public class EmergencyContact {

    //Initialize public variables
    public String fname, lname, phone, email, relation;

    //Initialize empty constructor
    public EmergencyContact(){}

    //Initialize constructor
    public EmergencyContact(String first_name, String last_name,
                            String phone_number, String email,
                            String relationship){
        this.fname = first_name;
        this.lname = last_name;
        this.phone = phone_number;
        this.email = email;
        this.relation = relationship;
    }
}
