package me.myapplication.Models;

import java.io.Serializable;

/**
 * Created by Aurelien on 28/04/2018.
 */

public class Incident implements Serializable{

    final private int incidentID;
    final private int reporterdID;
    final private int locationID;
    final private int typeID;

    final private Importance importance;

    final private String title;
    final private String description;

    private String urlPhoto;


    public Incident(int incidentID, int reporterdID, int locationID, int typeID, Importance importance,
                    String title, String description, String url) {
        this.reporterdID = reporterdID;
        this.locationID = locationID;
        this.typeID = typeID;
        this.importance = importance;
        this.title = title;
        this.description = description;
        this.urlPhoto = url;
        this.incidentID=incidentID;
    }


    public int getReporterdID() {
        return reporterdID;
    }

    public int getLocationID() {
        return locationID;
    }

    public int getTypeID() {
        return typeID;
    }

    public Importance getImportance() {
        return importance;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public int getIncidentID() {
        return incidentID;
    }
}
