package me.myapplication.Models;

import java.io.Serializable;
import java.util.Date;

public class PlanningIncident implements Serializable{

    final private int incidentId;
    final private String title;
    final private String lieuName;
    final private String typeName;
    final private Date startDate;
    final private Date endDate;
    final private int importance;

    public PlanningIncident(int incidentId, String title, String lieuName, String typeName, Date startDate, Date endDate, int importance) {
        this.incidentId = incidentId;
        this.title = title;
        this.lieuName = lieuName;
        this.typeName = typeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.importance = importance;
    }

    public String getTitle() {
        return title;
    }

    public String getLieuName() {
        return lieuName;
    }

    public String getTypeName() {
        return typeName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getImportance() {
        return importance;
    }

    public int getIncidentId() {
        return incidentId;
    }
}
