package me.myapplication.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.myapplication.Fragments.DayFragment;
import me.myapplication.Fragments.DayFragment.OnListFragmentInteractionListener;
import me.myapplication.Models.Incident;
import me.myapplication.R;
import me.myapplication.Fragments.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DayFragment}. */
public class ListDayRecyclerViewAdapter extends RecyclerView.Adapter<ListDayRecyclerViewAdapter.ViewHolder> {

   // private final List<DayFragment> days;

    public ListDayRecyclerViewAdapter(/*List<DayFragment> items*/) {
      //  days = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_planning_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
       // holder.mIncidentList=DayFragment
        holder.mdate.setText("Nous somme le 23/11/12");
    }

    @Override
    public int getItemCount() {
    //    return mValues.size();
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      //  public final View mView;
        TextView mdate;
       // public final RecyclerView mIncidentList;

        public ViewHolder(View view) {
            super(view);
            mdate=(TextView) itemView.findViewById(R.id.date);
            //mIncidentList= (RecyclerView) view.findViewById(R.id.planning_day);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
