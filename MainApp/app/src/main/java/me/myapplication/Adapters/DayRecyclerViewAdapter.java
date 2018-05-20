package me.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.myapplication.DeclarationActivity;
import me.myapplication.DisplayDetailsIncidentActivity;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.MainActivity;
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
    Context context;

    public DayRecyclerViewAdapter(Context context, int userId) {
        this.context=context;
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

        holder.titre.setText(planningIncident.getTitle());
        holder.lieu.setText(planningIncident.getLieuName());
        holder.type.setText(planningIncident.getTypeName());

        holder.planningIncident.setOnClickListener(new DetailsListener());
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titre;
        TextView lieu;
        TextView type;
        ConstraintLayout planningIncident;

        public ViewHolder(View view) {
            super(view);
            titre = (TextView) itemView.findViewById(R.id.title);
            lieu = (TextView) itemView.findViewById(R.id.lieu);
            type = (TextView) itemView.findViewById(R.id.type);
            planningIncident = (ConstraintLayout) itemView.findViewById(R.id.planning_incident);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public class DetailsListener implements View.OnClickListener {

        @Override
        public void onClick(View view)  {
            Intent intent = new Intent(context,DeclarationActivity.class);
         //   intent.putExtra("incident",incident);
            context.startActivity(intent);
        }
    }

}
