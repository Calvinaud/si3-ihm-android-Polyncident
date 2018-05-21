package me.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import me.myapplication.R;


public class ProfileActivity extends Activity {

    private static final String ARG_SECTION_NUMBER = "section_number";

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

        addListeners();

        displayUserData();

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
        this.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0768596940"));
                startActivity(callIntent);
            }
        });

        this.smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", "0768596940");
                startActivity(Intent.createChooser(smsIntent, "SMS:"));
            }
        });

        this.mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                mailIntent.setType("message/rfc822");
                mailIntent.setData(Uri.parse("mailto:test@gmail.com"));
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Incident - Titre incident ");
                startActivity(Intent.createChooser(mailIntent, "MAIL:"));
            }
        });
    }

    private void displayUserData(){
        try {
            //can throw a null pointer exception
            int userId = getIntent().getExtras().getInt("userId");
            Cursor cursor = IncidentDBHelper.getSingleton().getUserCursor(userId);

            this.nameLabel.setText(cursor.getString(cursor.getColumnIndex("username")));
            this.roleLabel.setText(cursor.getString(cursor.getColumnIndex("roles")));

            cursor.close();
        }catch (NullPointerException e){
            Logger.getAnonymousLogger().severe(
                    "No <userId> has been provided to "+getClass().getName()
            );
        }catch(IncidentDBHelper.NoRecordException e){
            Logger.getAnonymousLogger().severe(e.toString());
        }
        finally {
            // finish();
        }

    }
}
