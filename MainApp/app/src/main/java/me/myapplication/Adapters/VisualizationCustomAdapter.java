package me.myapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.myapplication.IncidentViewHolder;
import me.myapplication.Models.Incident;
import me.myapplication.R;

/**
 * Created by user on 29/04/2018.
 */

public class VisualizationCustomAdapter extends RecyclerView.Adapter<IncidentViewHolder> {
    @NonNull
    @Override
    public IncidentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }




    /*List<Incident> incidents;

    public VisualizationCustomAdapter(Context context, List<Incident> incidentList){
        super(context,0,incidentList);
        this.incidents = incidentList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Incident i = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.card_layout_visualization, null);
        }
        ImageView status = (ImageView) convertView.findViewById(R.id.statusView);

        TextView titleView = (TextView) convertView.findViewById(R.id.incidentTextView);
        TextView categoryView = (TextView) convertView.findViewById(R.id.timeTextView);
        TextView dateView = (TextView) convertView.findViewById(R.id.dateTextView);
        TextView emergencyView = (TextView) convertView.findViewById(R.id.emergencyTextView);
        TextView commentsView = (TextView) convertView.findViewById(R.id.commentNumberTextView);


        titleView.setText(i.getTitle());
        categoryView.setText(i.getDescription());
        if(i.getStatus().equals("vu")) {
            status.setImageResource(R.drawable.ic_remove_red_eye_black_24dp);
        }

        return convertView;
    }
    */
}
