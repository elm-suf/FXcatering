package catering.businesslogic.grasp_controllers;

import java.util.Date;

public class Shift {
    //public enum Type {service, kitchen};
    //private Type type;
    private int id;
    private Date date;
    private String type;

    public Shift(int id, Date date, String type) {
        this.id = id;
        this.date = date;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return date + " - " + type;
    }

    public int getId() {
        return id;
    }
}
