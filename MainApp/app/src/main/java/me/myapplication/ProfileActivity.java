package me.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.MainActivity;
import me.myapplication.Models.Importance;
import me.myapplication.Models.User;
import me.myapplication.R;


public class ProfileActivity extends Activity {

    private User user;
    private boolean hasTelephoneNumber;
    private boolean hasEmailAddress;

    //GUI components
    private TextView nameLabel;
    private TextView roleLabel;
    private ImageButton callButton;
    private ImageButton smsButton;
    private ImageButton mailButton;
    private ImageButton addContactButton;

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_profile);

        assignViewMembers();

        queryUserData();

        displayDataAndButtons();

        addListeners();

    }

    private void assignViewMembers(){
        this.nameLabel = findViewById(R.id.profile_name_label);
        this.roleLabel = findViewById(R.id.profile_role_label);

        this.callButton = findViewById(R.id.profile_call_button);
        this.smsButton = findViewById(R.id.profile_sms_button);
        this.mailButton = findViewById(R.id.profile_mail_button);
        this.addContactButton = findViewById(R.id.profile_add_contact_button);
    }

    private void addListeners(){

        if (this.callButton.getVisibility() == View.VISIBLE){
            this.callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+user.getTelephoneNumber()));
                    startActivity(callIntent);
                }
            });
        }

        if (this.smsButton.getVisibility() == View.VISIBLE) {

            this.smsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", user.getTelephoneNumber());
                    startActivity(Intent.createChooser(smsIntent, "SMS:"));
                }
            });
        }

        if (this.mailButton.getVisibility() == View.VISIBLE) {

            this.mailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                    mailIntent.setType("message/rfc822");
                    mailIntent.setData(Uri.parse("mailto:" + user.getEmailAddress()));
                    mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Incident - Titre incident ");
                    startActivity(Intent.createChooser(mailIntent, "MAIL:"));
                }
            });
        }

        this.addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addContactIntent =  new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                addContactIntent.putExtra(ContactsContract.Intents.Insert.NAME, user.getUsername());

                if(hasEmailAddress)
                    addContactIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, user.getEmailAddress());
                if(hasTelephoneNumber)
                    addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, user.getTelephoneNumber());

                startActivity(addContactIntent);
            }
        });
    }

    private void queryUserData(){
        try {
            //can throw a null pointer exception
            int userId = getIntent().getExtras().getInt("userId");
            Cursor cursor = IncidentDBHelper.getSingleton().getUserCursor(userId);

            this.user = new User(cursor);
            this.hasEmailAddress = (this.user.getEmailAddress() != null);
            this.hasTelephoneNumber = (this.user.getTelephoneNumber() != null);

        }catch (NullPointerException e){
            Logger.getAnonymousLogger().severe(
                    "No <userId> has been provided to "+getClass().getName()
            );
        }catch(IncidentDBHelper.NoRecordException e){
            Logger.getAnonymousLogger().severe(e.toString());
        }
    }

    private void displayDataAndButtons(){

        this.nameLabel.setText(user.getUsername());
        this.roleLabel.setText(user.getRole());


        if (!this.hasEmailAddress){
            this.mailButton.setVisibility(View.GONE);
        }

        if (!this.hasTelephoneNumber){
            this.smsButton.setVisibility(View.GONE);
            this.callButton.setVisibility(View.GONE);
        }

    }
}
