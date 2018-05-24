package me.myapplication.Adapters;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;

import me.myapplication.AdminActivity;
import me.myapplication.AdminPlanningActivity;
import me.myapplication.Helpers.CalendarQueryHandler;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.MainActivity;
import me.myapplication.Models.Importance;
import me.myapplication.Models.Incident;
import me.myapplication.R;

public class TechosRecyclerViewAdapter extends RecyclerView.Adapter<TechosRecyclerViewAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Cursor cursor);
    }

    private Context context;
    private Cursor userCursor;
    private final OnItemClickListener listener;

    public TechosRecyclerViewAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        userCursor = IncidentDBHelper.getSingleton().getTechnicien();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_technicien, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        this.userCursor.moveToPosition(position);

        String status = "Status: ";
        String todaycharge = "-Aujourd'hui: ";
        String weekcharge = "-Cette semaine: ";
        String username = userCursor.getString(userCursor.getColumnIndex("username"));
        int id = userCursor.getInt(userCursor.getColumnIndex("userId"));

        if (IncidentDBHelper.getSingleton().getTechoStatus(id)) {
            status += "Occupé";
        } else {
            status += "Libre";
        }

        long daymilli = IncidentDBHelper.getSingleton().getTechoDaySum(id);
        String hours = String.format("%02d", TimeUnit.MILLISECONDS.toHours(daymilli));
        daymilli -= TimeUnit.HOURS.toMillis(TimeUnit.MILLISECONDS.toHours(daymilli));
        String min = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(daymilli));

        todaycharge += hours + "h" + min + " (" + IncidentDBHelper.getSingleton().getTechoDayCount(id) + " tâches)";

        long weekmilli = IncidentDBHelper.getSingleton().getTechoWeekSum(id);
        hours = String.format("%02d", TimeUnit.MILLISECONDS.toHours(weekmilli));
        weekmilli -= TimeUnit.HOURS.toMillis(TimeUnit.MILLISECONDS.toHours(weekmilli));
        min = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(weekmilli));

        weekcharge += hours + "h" + min + " (" + IncidentDBHelper.getSingleton().getTechoWeekCount(id) + " tâches)";

        holder.username.setText(username);
        holder.todaycharge.setText(todaycharge);
        holder.status.setText(status);
        holder.weekcharge.setText(weekcharge);
        holder.bind(userCursor, listener);
    }

    @Override public int getItemCount() {
        return userCursor.getCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View card;
        TextView status;
        TextView username;
        TextView todaycharge;
        TextView weekcharge;

        public ViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.card);
            username = (TextView) itemView.findViewById(R.id.Username);
            todaycharge = (TextView) itemView.findViewById(R.id.todayCharge);
            status = (TextView) itemView.findViewById(R.id.status);
            weekcharge = (TextView) itemView.findViewById(R.id.weekCharge);
        }

        public void bind(final Cursor cursor, final OnItemClickListener listener) {
            card.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(cursor);
                }
            });
        }
    }
}