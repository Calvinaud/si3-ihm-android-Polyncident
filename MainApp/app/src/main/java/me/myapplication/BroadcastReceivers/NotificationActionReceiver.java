package me.myapplication.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.logging.Level;
import java.util.logging.Logger;

import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.Incident;

public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action=intent.getStringExtra("action");
        int incidentId = intent.getIntExtra("incidentId", -1);

        Logger.getAnonymousLogger().log(Level.WARNING,"tio"+action);
        if(action.equals("S'ABONNER")){
            IncidentDBHelper.getSingleton().insertSub(0, incidentId ); //a changer

        }
        else if(action.equals("action2")){
            performAction2();
        }
        Intent intent1 = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(intent1);
    }

    public void performAction2(){

    }
}
