package me.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
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

/**
 * Created by user on 29/04/2018.
 */

public class VisualizationCustomAdapter extends RecyclerView.Adapter<VisualizationCustomAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;
    private int incidentID;
    private final int userId;

    public VisualizationCustomAdapter(Context context, Boolean myIncident, int userId) {
        this.context = context;
        this.userId = userId;
        if (myIncident) {
            this.cursor = IncidentDBHelper.getSingleton().getIncidentCursor(userId, -1, -1, 100);
        } else {
            this.cursor = IncidentDBHelper.getSingleton().getIncidentCursor(-1, -1, -1, 100);
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
                cursor.moveToFirst();
                Cursor cursorId = IncidentDBHelper.getSingleton().getIncidentCursor(-1, -1, -1, 100);
                cursorId.moveToPosition(holder.getAdapterPosition());
                int incidentId = cursorId.getInt(cursorId.getColumnIndexOrThrow("incidentId"));
                Intent intent = new Intent(context,DisplayDetailsIncidentActivity.class);
                Log.i("id: ","");
                intent.putExtra("incidentId",incidentId);
                view.getContext().startActivity(intent);

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
        String desc = cursor.getString(cursor.getColumnIndex("description"));
        int reporterId = cursor.getInt(cursor.getColumnIndexOrThrow("reporterId"));
        int locationId = cursor.getInt(cursor.getColumnIndexOrThrow("locationId"));
        int typeId = cursor.getInt(cursor.getColumnIndexOrThrow("typeId"));
        //String url = cursor.getString(cursor.getColumnIndexOrThrow("urlPhoto"));
        Log.i("id2: ",""+incidentID);
        holder.incident.setText(title);
        String fulldate = cursor.getString(cursor.getColumnIndexOrThrow("declarationDate"));

        String[] date=fulldate.split(" ");

        holder.date.setText(date[0]);

        String[] hourParts = date[1].split(":");

        holder.heure.setText(hourParts[0] + ":" + hourParts[1]);

        holder.urgence.setText(urgence.getText());
        holder.description.setText(desc);

        holder.itemView.findViewById(R.id.card_border)
                       .setBackgroundColor(urgence.getColor(context));

        Logger.getAnonymousLogger().log(Level.WARNING,"status ="+cursor.getInt(cursor.getColumnIndexOrThrow("status")));
        switch (cursor.getInt(cursor.getColumnIndexOrThrow("status"))){
            case 0:
                holder.status.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_visibility_off_black_24dp));
                break;
            case 1:
                holder.status.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_watch_later_black_24dp));
                break;
            case 2:
                holder.status.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_check_circle_black_24dp));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
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

        TextView incident;
        TextView date;
        TextView heure;
        TextView urgence;
        TextView description;
        TextView commentNumer;
        CardView cardView;

        ViewHolder(View itemView) {

            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card);
            status=(ImageView) itemView.findViewById(R.id.statusView);
            subscribe=(ImageView) itemView.findViewById(R.id.subscribeView);

            incident=(TextView) itemView.findViewById(R.id.incidentTextView);
            date=(TextView) itemView.findViewById(R.id.dateTextView);
            heure=(TextView) itemView.findViewById(R.id.timeTextView);
            urgence=(TextView) itemView.findViewById(R.id.emergencyTextView);
            description=(TextView) itemView.findViewById(R.id.descView);
            commentNumer=(TextView) itemView.findViewById(R.id.unreadCommentNumber);

            subscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!subscribed) {
                        subscribe.setImageResource(R.drawable.ic_star_black_24dp);
                        subscribed = true;
                        IncidentDBHelper.getSingleton().insertSub(userId,cursor.getInt(cursor.getColumnIndexOrThrow("incidentId")));
                    }else {
                        subscribe.setImageResource(R.drawable.ic_star_border_black_24dp);
                        subscribed=false;
                    }


                }
            });

        }
    }

}
