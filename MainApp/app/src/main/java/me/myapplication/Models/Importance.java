package me.myapplication.Models;

import android.content.Context;
import android.graphics.Color;

import me.myapplication.R;

/**
 * Created by Aurelien on 28/04/2018.
 */

public enum Importance {

    NEGLIGIBLE("Négligeable"), MINOR("Mineur"), URGENT("Forte"), CRITICAL("Urgente");

    private String text;
    static private Color[] colors;

    Importance(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public int getColor(Context context){
        return context.getResources().getIntArray(R.array.importanceColors)[ordinal()];

    }


    public static Importance getImportanceByValue(int value){

        value--;

        if (value < 0 || value >= values().length){
            throw new IllegalArgumentException(
                    "There are only "+values().length + " possible importance values"
            );
        }

        return values()[value];
    }
}
