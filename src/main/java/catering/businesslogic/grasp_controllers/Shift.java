package catering.businesslogic.grasp_controllers;

import java.util.Date;

public class Shift {
    //public enum Type {service, kitchen};
    //private Type type;
    private Date date;
    private String type;

    public Shift(Date date, String type) {
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
}
