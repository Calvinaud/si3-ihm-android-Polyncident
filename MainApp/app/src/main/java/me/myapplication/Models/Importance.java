package me.myapplication.Models;

/**
 * Created by Aurelien on 28/04/2018.
 */

public enum Importance {

    NEGLIGIBLE("négligeable"), MINOR("mineur"), URGENT("assez urgent"), CRITICAL("très urgent");

    private String text;

    Importance(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }


    public static Importance getImportanceByValue(int value){
        if (value < 0 || value >= values().length){
            throw new IllegalArgumentException(
                    "There are only "+values().length + " possible importance values"
            );
        }

        return values()[value];
    }
}
