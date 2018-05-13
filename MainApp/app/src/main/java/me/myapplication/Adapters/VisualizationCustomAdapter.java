package me.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.myapplication.DisplayIncidentActivity;
import me.myapplication.Fragments.VisualizationFragment;
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
    private IncidentDBHelper incidentDBHelper;
    private Cursor cursor;
    private Incident incident;

    public VisualizationCustomAdapter(Context context, IncidentDBHelper incidentDBHelper){
        this.context=context;
        this.incidentDBHelper = incidentDBHelper;
        this.cursor=incidentDBHelper.getIncidentCursor(-1,-1,-1,100);
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

        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String desc = cursor.getString(cursor.getColumnIndex("description"));
        //Importance importance; A FAIRE
        String urgence = cursor.getString(cursor.getColumnIndex("importance")); //temp
        int reporterId = cursor.getInt(cursor.getColumnIndexOrThrow("reporterId"));
        int locationId = cursor.getInt(cursor.getColumnIndexOrThrow("locationId"));
        int typeId = cursor.getInt(cursor.getColumnIndexOrThrow("typeId"));

        this.incident=new Incident(reporterId,locationId,typeId,Importance.MINOR,title,desc);
        holder.cardView.setOnClickListener(new DetailsListener());
        holder.incident.setText(title);
        holder.date.setText("0");
        holder.heure.setText("0");
        holder.urgence.setText(urgence);
        holder.description.setText(desc);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView status;
        ImageView comments;

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
            comments=(ImageView) itemView.findViewById(R.id.commentsView);

            incident=(TextView) itemView.findViewById(R.id.incidentTextView);
            date=(TextView) itemView.findViewById(R.id.dateTextView);
            heure=(TextView) itemView.findViewById(R.id.timeTextView);
            urgence=(TextView) itemView.findViewById(R.id.emergencyTextView);
            description=(TextView) itemView.findViewById(R.id.descView);
        }
    }

    public class DetailsListener implements View.OnClickListener {

        @Override
        public void onClick(View view)  {
            Log.i("heu","yghefjn");
            Intent intent = new Intent(context,DisplayIncidentActivity.class);
            intent.putExtra("incident",incident);
            context.startActivity(intent);
        }
    }
}
