package me.myapplication.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.Importance;
import me.myapplication.R;

/**
 * Created by Aurelien on 08/05/2018.
 */

public class ProfileFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_DB_HELPER = "dbhelper_object";

    private IncidentDBHelper dbHelper;

    //GUI components
    private TextView nameLabel;
    private ImageButton callButton;
    private  ImageButton smsButton;
    private  ImageButton mailButton;

    public static DeclarationFragment newInstance(int sectionNumber, IncidentDBHelper dbHelper) {
        DeclarationFragment fragment = new DeclarationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(ARG_DB_HELPER, dbHelper);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.dbHelper = (IncidentDBHelper)getArguments().getSerializable(ARG_DB_HELPER);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_declaration, container, false);
        this.nameLabel = rootView.findViewById(R.id.profile_name_label);
        this.callButton = rootView.findViewById(R.id.profile_call_button);
        this.smsButton = rootView.findViewById(R.id.profile_sms_button);
        this.mailButton = rootView.findViewById(R.id.profile_mail_button);

        return rootView;
    }

}
