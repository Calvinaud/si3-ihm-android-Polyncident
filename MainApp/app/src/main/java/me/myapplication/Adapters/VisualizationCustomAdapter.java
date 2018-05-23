package me.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import me.myapplication.MainActivity;
import me.myapplication.Models.Importance;
import me.myapplication.Models.Incident;
import me.myapplication.R;

/**
 * Created by user on 29/04/2018.
 */

public class VisualizationCustomAdapter extends RecyclerView.Adapter<VisualizationCustomAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;
    private Incident incident;
    private Boolean subscribed;

    public VisualizationCustomAdapter(Context context, Boolean myIncident) {
        this.context = context;
        int myReporterId = 0;
        if (myIncident) {
            this.cursor = IncidentDBHelper.getSingleton().getIncidentCursor(myReporterId, -1, -1, 100);
        } else {
            this.cursor = IncidentDBHelper.getSingleton().getIncidentCursor(-1, -1, -1, 100);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_visualization, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        this.cursor.moveToPosition(position);
        int incidentID = cursor.getInt(cursor.getColumnIndexOrThrow("incidentId"));
        if((this.subscribed = IncidentDBHelper.getSingleton().isUserSubscribed(0,incidentID))){
            holder.subscribe.setImageResource(R.drawable.ic_star_black_24dp);
        }
        Importance urgence = getImportance(cursor.getInt(cursor.getColumnIndex("importance")));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String desc = cursor.getString(cursor.getColumnIndex("description"));
        int reporterId = cursor.getInt(cursor.getColumnIndexOrThrow("reporterId"));
        int locationId = cursor.getInt(cursor.getColumnIndexOrThrow("locationId"));
        int typeId = cursor.getInt(cursor.getColumnIndexOrThrow("typeId"));
        String url = cursor.getString(cursor.getColumnIndexOrThrow("urlPhoto"));

        this.incident=new Incident(reporterId,locationId,typeId,urgence,title,desc,url);
        holder.cardView.setOnClickListener(new DetailsListener());
        holder.incident.setText(title);
        holder.date.setText("0");
        holder.heure.setText("0");
        holder.urgence.setText(urgence.getText());
        holder.description.setText(desc);

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

            subscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!subscribed) {
                        subscribe.setImageResource(R.drawable.ic_star_black_24dp);
                        subscribed = true;
                        IncidentDBHelper.getSingleton().insertSub(0,cursor.getInt(cursor.getColumnIndexOrThrow("incidentId")));
                    }else {
                        subscribe.setImageResource(R.drawable.ic_star_border_black_24dp);
                        subscribed=false;
                    }
                }
            });

        }
    }


    public class DetailsListener implements View.OnClickListener {

        @Override
        public void onClick(View view)  {
            Intent intent = new Intent(context,DisplayDetailsIncidentActivity.class);
            intent.putExtra("incident",incident);
            context.startActivity(intent);
        }
    }
}
