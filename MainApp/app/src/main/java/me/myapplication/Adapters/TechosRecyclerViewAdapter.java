package me.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.myapplication.DisplayDetailsIncidentActivity;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.Importance;
import me.myapplication.Models.Incident;
import me.myapplication.R;

public class TechosRecyclerViewAdapter extends RecyclerView.Adapter<TechosRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private Cursor userCursor;

    public TechosRecyclerViewAdapter(Context context) {
        this.context = context;
        userCursor = IncidentDBHelper.getSingleton().getTechnicien();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_technicien, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        this.userCursor.moveToPosition(position);

        String status="Status: ";
        String todaycharge="-Aujourd'hui: ";
        String weekcharge="-Cette semaine: ";
        String username=userCursor.getString(userCursor.getColumnIndex("username"));
        int id = userCursor.getInt(userCursor.getColumnIndex("userId"));

        if(IncidentDBHelper.getSingleton().getTechoStatus(id)){
            status += "Occupé";
        }
        else {
            status += "Libre";
        }

        long daymilli = IncidentDBHelper.getSingleton().getTechoDaySum(id);
        String time=String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(daymilli),
                TimeUnit.MILLISECONDS.toMinutes(daymilli) -
                        TimeUnit.MINUTES.toMinutes(TimeUnit.MILLISECONDS.toHours(daymilli)));

        todaycharge += time+" ("+IncidentDBHelper.getSingleton().getTechoDayCount(id)+" tâches)";

        holder.username.setText(username);
        holder.todaycharge.setText(todaycharge);
        holder.status.setText(status);
        holder.weekcharge.setText(weekcharge);
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView status;
        TextView username;
        TextView todaycharge;
        TextView weekcharge;

        ViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.Username);
            todaycharge = (TextView) itemView.findViewById(R.id.todayCharge);
            status = (TextView) itemView.findViewById(R.id.status);
            weekcharge = (TextView) itemView.findViewById(R.id.weekCharge);

        }
    }

    /*public class DetailsListener implements View.OnClickListener {

        @Override
        public void onClick(View view)  {
            Intent intent = new Intent(context,DisplayDetailsIncidentActivity.class);
            intent.putExtra("incident",incident);
            context.startActivity(intent);
        }
    }*/
}
