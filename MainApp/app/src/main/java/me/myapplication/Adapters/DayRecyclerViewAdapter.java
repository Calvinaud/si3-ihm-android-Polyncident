package me.myapplication.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.PlanningIncident;
import me.myapplication.PlanningActivity;
import me.myapplication.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter}
 */
public class DayRecyclerViewAdapter extends RecyclerView.Adapter<DayRecyclerViewAdapter.ViewHolder> {

    List<PlanningIncident> incidents;
    int userId;

    public DayRecyclerViewAdapter(int userId) {
        this.userId=userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_planning_incident, parent, false);

        incidents = new ArrayList<>();
        incidents.addAll(IncidentDBHelper.getSingleton().getDayPlanningIncident(userId));

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        PlanningIncident planningIncident = incidents.get(position);

        holder.text1.setText(planningIncident.getLieuName());
        holder.text2.setText(planningIncident.getTypeName());
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text1;
        TextView text2;
        TextView text3;

        public ViewHolder(View view) {
            super(view);
            text1= (TextView) itemView.findViewById(R.id.item_number);
            text2= (TextView) itemView.findViewById(R.id.content);
            text3= (TextView) itemView.findViewById(R.id.truc);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
