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

import me.myapplication.DisplayDetailsIncidentActivity;
import me.myapplication.Fragments.VisualizationFragment;
import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.MainActivity;
import me.myapplication.Models.Importance;
import me.myapplication.Models.Incident;
import me.myapplication.R;

/**
 * Created by user on 29/04/2018.
 */

public class DisplayCommentariesAdapter extends RecyclerView.Adapter<DisplayCommentariesAdapter.ViewHolder> {

    private Context context;

    public DisplayCommentariesAdapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_commentaire, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.profilName.setText("NAME");
        holder.date.setText("0");
        holder.heure.setText("0");
        holder.content.setText("CONTENT OF THE COMMENT");
    }

    @Override
    public int getItemCount() {
        return 3;
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profilPicture;

        TextView content;
        TextView date;
        TextView heure;
        TextView profilName;

        ViewHolder(View itemView) {

            super(itemView);

            profilPicture = (ImageView) itemView.findViewById(R.id.profilPicture);
            content = (TextView) itemView.findViewById(R.id.commentary);
            date=(TextView) itemView.findViewById(R.id.dateTextView2);
            heure=(TextView) itemView.findViewById(R.id.timeTextView2);
            profilName=(TextView) itemView.findViewById(R.id.profilName);
        }
    }

}
