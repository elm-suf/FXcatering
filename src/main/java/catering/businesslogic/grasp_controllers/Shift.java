package catering.businesslogic.grasp_controllers;

import java.util.Date;
import java.util.Objects;

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

    public Shift() {

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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return date + " - " + type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shift shift = (Shift) o;
        return getId() == shift.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
