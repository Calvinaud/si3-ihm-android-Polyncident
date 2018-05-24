package me.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
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
    private Cursor cursorComms;
    int i=0;
    public DisplayCommentariesAdapter(Context context, int incidentId) {
        this.context=context;
        this.cursorComms=IncidentDBHelper.getSingleton().getCommentaires(incidentId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_commentaire, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            this.cursorComms.moveToPosition(position);
            Log.i("n",""+this.cursorComms.getCount());
            String contentComment = this.cursorComms.getString(this.cursorComms.getColumnIndexOrThrow("comment"));
            String fulldate = this.cursorComms.getString(this.cursorComms.getColumnIndexOrThrow("date"));
            String[] date=fulldate.split(" ");
            String username = this.cursorComms.getString(this.cursorComms.getColumnIndexOrThrow("username"));
            int userId= this.cursorComms.getInt(this.cursorComms.getColumnIndexOrThrow("userId"));

        try {
            Cursor cursorUser = IncidentDBHelper.getSingleton().getUserCursor(userId);
            cursorUser.moveToFirst();
            if(cursorUser.getString(cursorUser.getColumnIndexOrThrow("roles")).equals("ADMINISTRATEUR")){
                holder.profilPicture.setImageResource(R.mipmap.director);
            }
            if(cursorUser.getString(cursorUser.getColumnIndexOrThrow("roles")).equals("UTILISATEUR")){
                holder.profilPicture.setImageResource(R.mipmap.etudiant);
            }
            if(cursorUser.getString(cursorUser.getColumnIndexOrThrow("roles")).equals("TECHNICIEN")){
                holder.profilPicture.setImageResource(R.mipmap.technicien);
            }

        } catch (IncidentDBHelper.NoRecordException e) {
            e.printStackTrace();
        }
        holder.profilName.setText(username);
            holder.date.setText(date[0]);
            holder.heure.setText(date[1]);
            holder.content.setText(contentComment);
            i++;
    }

    @Override
    public int getItemCount() {
        if (cursorComms!=null) return cursorComms.getCount();
        return 0;
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
