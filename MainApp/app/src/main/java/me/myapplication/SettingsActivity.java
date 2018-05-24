package me.myapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    Switch notificationEnabler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        notificationEnabler = (Switch) findViewById(R.id.enableNotification);

        notificationEnabler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIsEnabled()){
                    storeIsEnable(false);
                }
                else {
                    storeIsEnable(true);
                }
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
        boolean enabled = sharedPreferences.getBoolean("Enabled", false);
        return enabled;
    }
}
