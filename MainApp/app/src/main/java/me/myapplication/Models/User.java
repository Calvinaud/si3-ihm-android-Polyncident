package me.myapplication.Models;

import android.database.Cursor;

import java.util.logging.Logger;

public class User {

    private String username;
    private String emailAddress;
    private String telephoneNumber;
    private String role;

    public User(Cursor cursor){
        if(cursor.getCount() != 1)
            throw new IllegalArgumentException("The cursor should contains only one row");

        this.username = cursor.getString(cursor.getColumnIndex("username"));
        this.emailAddress = cursor.getString(cursor.getColumnIndex("emailAddress"));
        this.telephoneNumber = cursor.getString(cursor.getColumnIndex("telephoneNumber"));
        this.role = cursor.getString(cursor.getColumnIndex("roles"));

        cursor.close();
    }

    public String getUsername() {
        return username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getRole() {
        return role;
    }

}
