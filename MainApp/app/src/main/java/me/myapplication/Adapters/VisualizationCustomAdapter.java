package me.myapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import me.myapplication.Models.Incident;
import me.myapplication.R;

/**
 * Created by user on 29/04/2018.
 */

public class VisualizationCustomAdapter extends RecyclerView.Adapter<VisualizationCustomAdapter.ViewHolder> {

    private List<Incident> incidentList;
    private Context context;

    public VisualizationCustomAdapter(List<Incident> incidentList, Context context){
        this.incidentList=incidentList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_visualization, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Incident incident = incidentList.get(position);

        //holder.incident.setText("ok");
        holder.date.setText("0");
        holder.heure.setText("0");
        holder.urgence.setText("0");
        holder.commentsNumber.setText("0");
    }

    @Override
    public int getItemCount() {
        return this.incidentList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView status;
        public ImageView comments;

        public TextView incident;
        public TextView date;
        public TextView heure;
        public TextView urgence;
        public TextView commentsNumber;

        public ViewHolder(View itemView) {
            super(itemView);

            status=(ImageView) itemView.findViewById(R.id.statusView);
            comments=(ImageView) itemView.findViewById(R.id.commentsView);

            incident=(TextView) itemView.findViewById(R.id.incidentTextView);
            date=(TextView) itemView.findViewById(R.id.dateTextView);
            heure=(TextView) itemView.findViewById(R.id.timeTextView);
            urgence=(TextView) itemView.findViewById(R.id.emergencyTextView);
            commentsNumber=(TextView) itemView.findViewById(R.id.commentNumberTextView);
        }
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
