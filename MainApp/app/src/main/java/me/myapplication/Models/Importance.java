package me.myapplication.Models;

/**
 * Created by Aurelien on 28/04/2018.
 */

enum Importance {

    NEGLIGIBLE("négligeable"), MINOR("mineur"), URGENT("urgent"), CRITICAL("très urgent");

    private String text;

    Importance(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
