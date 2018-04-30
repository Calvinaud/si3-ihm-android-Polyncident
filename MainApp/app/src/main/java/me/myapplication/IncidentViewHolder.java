package me.myapplication;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 29/04/2018.
 */

public class IncidentViewHolder extends RecyclerView.ViewHolder {

    private ImageView status;
    private ImageView comments;

    private TextView title;

    public IncidentViewHolder(View itemView) {
        super(itemView);


    }
}
