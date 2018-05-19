package me.myapplication.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import me.myapplication.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter}
 * TODO: Replace the implementation with code for your data type.
 */
public class DayRecyclerViewAdapter extends RecyclerView.Adapter<DayRecyclerViewAdapter.ViewHolder> {

    public DayRecyclerViewAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_planning_incident, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.text1.setText("Test1");
        holder.text2.setText("Test22");
        holder.text3.setText("Test333");
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
