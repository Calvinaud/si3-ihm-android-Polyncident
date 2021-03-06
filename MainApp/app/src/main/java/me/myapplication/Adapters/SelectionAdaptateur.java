package me.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.logging.Level;
import java.util.logging.Logger;

import me.myapplication.DisplayDetailsIncidentActivity;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.Importance;
import me.myapplication.R;

import static android.content.Context.MODE_PRIVATE;

public class SelectionAdaptateur extends RecyclerView.Adapter<SelectionAdaptateur.ViewHolder> {

    private Context context;
    private Cursor cursor;
    private int incidentID;
    private final int userId;

    public SelectionAdaptateur(Context context, Boolean myIncident, int userId, int importance, int type) {
        this.context = context;
        this.userId = userId;

        if (myIncident) {
            this.cursor = IncidentDBHelper.getSingleton().getIncidentCursor(userId, type, importance, 100);
        } else {
            this.cursor = IncidentDBHelper.getSingleton().getIncidentCursor(-1, type, importance, 100);
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_visualization, parent, false);
        final ViewHolder holder = new ViewHolder(v);

        SharedPreferences sharedPreferences = context.getSharedPreferences("Comments", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Integer.toString(incidentID), IncidentDBHelper.getSingleton().getCommentaires(incidentID).getCount());
        editor.apply();
        Logger.getAnonymousLogger().log(Level.WARNING,"COMMS"+IncidentDBHelper.getSingleton().getCommentaires(incidentID).getCount());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardView.setCardBackgroundColor(Color.RED);
            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        this.cursor.moveToPosition(position);
        incidentID = cursor.getInt(cursor.getColumnIndexOrThrow("incidentId"));

        if(IncidentDBHelper.getSingleton().isUserSubscribed(userId,incidentID)){
            SharedPreferences sharedPreferences = context.getSharedPreferences("Comments", MODE_PRIVATE);
            int prev = sharedPreferences.getInt(Integer.toString(incidentID), 0);
            int current = IncidentDBHelper.getSingleton().getCommentaires(incidentID).getCount();

            int commNumber = current-prev;
            Logger.getAnonymousLogger().log(Level.WARNING," TIO"+commNumber);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(Integer.toString(incidentID), IncidentDBHelper.getSingleton().getCommentaires(incidentID).getCount());
            editor.apply();

            if(commNumber>=0){
                holder.commentNumer.setText(Integer.toString(commNumber));
            }else {holder.commentNumer.setText(Integer.toString(1));}


            holder.subscribe.setImageResource(R.drawable.ic_star_black_24dp);
        }
        Importance urgence = getImportance(cursor.getInt(cursor.getColumnIndex("importance")));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));


        int reporterId = cursor.getInt(cursor.getColumnIndexOrThrow("reporterId"));
        int locationId = cursor.getInt(cursor.getColumnIndexOrThrow("locationId"));
        int typeId = cursor.getInt(cursor.getColumnIndexOrThrow("typeId"));
        //String url = cursor.getString(cursor.getColumnIndexOrThrow("urlPhoto"));
        Log.i("id2: ",""+incidentID);
        holder.incident.setText(title);
        String fulldate = cursor.getString(cursor.getColumnIndexOrThrow("declarationDate"));

        reduceDate(holder, fulldate);
        String urgenceS="Urgence: "+urgence.getText();
        holder.urgence.setText(urgenceS);



        String lieu = "Lieu: "+ IncidentDBHelper.getSingleton().getLocationName(locationId);
        holder.lieu.setText(lieu);

        holder.itemView.findViewById(R.id.card_border)
                .setBackgroundColor(urgence.getColor(context));

        Logger.getAnonymousLogger().log(Level.WARNING,"status ="+cursor.getInt(cursor.getColumnIndexOrThrow("status")));
        switch (cursor.getInt(cursor.getColumnIndexOrThrow("status"))){
            case 0:
                holder.status.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_watch_later_black_24dp));
                holder.etat.setText("En attente");
                break;
            case 1:
                holder.status.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_sync_black_24dp));
                holder.etat.setText("En Cours");
                break;
            case 2:
                holder.status.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_check_circle_black_24dp));
                holder.etat.setText("Résolu");
                break;
        }
    }

    private void reduceDate(ViewHolder holder, String fulldate){

        String[] splittedFullDate = fulldate.split(" ");

        String[] dateParts = splittedFullDate[0].split("-");

        //display the month and the day
        String affichage = "Date: "+dateParts[1]+"-"+dateParts[2];

        holder.date.setText(affichage);


        String[] hourParts = splittedFullDate[1].split(":");

        holder.heure.setText(hourParts[0] + ":" + hourParts[1]);
    }

    @Override
    public int getItemCount() {
        if (cursor!=null) return cursor.getCount();
        return 0;
    }

    private Importance getImportance(int i){
        switch (i) {
            case 1:
                return Importance.NEGLIGIBLE;
            case 2 :
                return Importance.MINOR;
            case 3 :
                return Importance.URGENT;
            case 4 :
                return Importance.CRITICAL;
            default: return Importance.MINOR;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView status;
        ImageView subscribe;
        boolean subscribed = false;

        TextView etat;
        TextView incident;
        TextView date;
        TextView heure;
        TextView urgence;
        TextView lieu;
        TextView commentNumer;
        CardView cardView;

        ViewHolder(View itemView) {

            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card);
            status=(ImageView) itemView.findViewById(R.id.statusView);
            subscribe=(ImageView) itemView.findViewById(R.id.subscribeView);

            etat = (TextView) itemView.findViewById(R.id.etat);
            incident=(TextView) itemView.findViewById(R.id.incidentTextView);
            date=(TextView) itemView.findViewById(R.id.dateTextView);
            heure=(TextView) itemView.findViewById(R.id.timeTextView);
            urgence=(TextView) itemView.findViewById(R.id.emergencyTextView);
            lieu =(TextView) itemView.findViewById(R.id.lieuTextView);
            commentNumer=(TextView) itemView.findViewById(R.id.unreadCommentNumber);

            subscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }

}
