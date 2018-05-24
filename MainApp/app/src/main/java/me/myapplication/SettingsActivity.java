package me.myapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SettingsActivity extends AppCompatActivity {

    private Switch notificationEnabler;
    private Switch fourniture;
    private Switch others;
    private Switch mat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        notificationEnabler = (Switch) findViewById(R.id.enableNotification);
        notificationEnabler.setChecked(getIsEnabled());

        fourniture = (Switch) findViewById(R.id.switchFourniture);
        fourniture.setChecked(getIsEnabled1());

        others = (Switch) findViewById(R.id.switchOthers);
        others.setChecked(getIsEnabled2());


        mat = (Switch) findViewById(R.id.switchMat);
        mat.setChecked(getIsEnabled3());



        notificationEnabler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIsEnabled()){
                    storeIsEnable(false);
                }
                else {
                    storeIsEnable(true);
                }
                Logger.getAnonymousLogger().log(Level.WARNING,"FOURNI = "+getIsEnabled());

            }
        });

        fourniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIsEnabled1()){
                    storeIsEnable1(false);
                }
                else {
                    storeIsEnable1(true);
                }
                Logger.getAnonymousLogger().log(Level.WARNING,"FOURNI = "+getIsEnabled1());

            }
        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIsEnabled2()){
                    storeIsEnable2(false);
                }
                else {
                    storeIsEnable2(true);
                }
                Logger.getAnonymousLogger().log(Level.WARNING,"FOURNI = "+getIsEnabled2());

            }
        });

        mat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIsEnabled3()){
                    storeIsEnable3(false);
                }
                else {
                    storeIsEnable3(true);
                }
                Logger.getAnonymousLogger().log(Level.WARNING,"FOURNI = "+getIsEnabled2());

            }
        });

    }

    private void storeIsEnable(boolean enabled){
        SharedPreferences sharedPreferences = getSharedPreferences("Notifications", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Enabled", enabled);
        editor.apply();
    }

    private boolean getIsEnabled(){
        SharedPreferences sharedPreferences = getSharedPreferences("Notifications", MODE_PRIVATE);
        boolean enabled = sharedPreferences.getBoolean("Enabled", true);
        return enabled;
    }




    private void storeIsEnable1(boolean enabled){
        SharedPreferences sharedPreferences = getSharedPreferences("Notifications", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Furniture", enabled);
        editor.apply();
    }

    private boolean getIsEnabled1(){
        SharedPreferences sharedPreferences = getSharedPreferences("Notifications", MODE_PRIVATE);
        boolean enabled = sharedPreferences.getBoolean("Furniture", true);
        return enabled;
    }




    private void storeIsEnable2(boolean enabled){
        SharedPreferences sharedPreferences = getSharedPreferences("Notifications", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Others", enabled);
        editor.apply();
    }

    private boolean getIsEnabled2(){
        SharedPreferences sharedPreferences = getSharedPreferences("Notifications", MODE_PRIVATE);
        boolean enabled = sharedPreferences.getBoolean("Others", true);
        return enabled;
    }



    private void storeIsEnable3(boolean enabled){
        SharedPreferences sharedPreferences = getSharedPreferences("Notifications", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Mat", enabled);
        editor.apply();
    }

    private boolean getIsEnabled3(){
        SharedPreferences sharedPreferences = getSharedPreferences("Notifications", MODE_PRIVATE);
        boolean enabled = sharedPreferences.getBoolean("Mat", true);
        return enabled;
    }

}
