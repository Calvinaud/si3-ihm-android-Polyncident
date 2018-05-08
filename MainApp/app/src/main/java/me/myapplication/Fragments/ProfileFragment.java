package me.myapplication.Fragments;

import android.content.Intent;
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

import java.util.logging.Logger;

import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.MainActivity;
import me.myapplication.Models.Importance;
import me.myapplication.R;

/**
 * Created by Aurelien on 08/05/2018.
 */

public class ProfileFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private IncidentDBHelper dbHelper;

    //GUI components
    private TextView nameLabel;
    private ImageButton callButton;
    private  ImageButton smsButton;
    private  ImageButton mailButton;

    public static ProfileFragment newInstance(int sectionNumber, IncidentDBHelper dbHelper) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.dbHelper = ((MainActivity)getContext()).getDBHelper();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0768596940"));
                startActivity(callIntent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        Logger.getAnonymousLogger().warning("onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        this.nameLabel = rootView.findViewById(R.id.profile_name_label);
        this.callButton = rootView.findViewById(R.id.profile_call_button);
        this.smsButton = rootView.findViewById(R.id.profile_sms_button);
        this.mailButton = rootView.findViewById(R.id.profile_mail_button);

        return rootView;
    }

}
