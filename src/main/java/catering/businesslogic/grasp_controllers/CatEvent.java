package catering.businesslogic.grasp_controllers;

import java.util.List;
import java.util.Objects;

public class CatEvent {
    private int id;
    private String name;
    private User chef;
    private int menuId;
    private List<Task> tasks;

    public CatEvent(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CatEvent(int id, String name, int menuId) {
        this.id = id;
        this.name = name;
        this.menuId = menuId;
    }

    public CatEvent(int id, String name, int menuId, User chef) {
        this.id = id;
        this.name = name;
        this.menuId = menuId;
        this.chef = chef;

    }

    public User getChef() {
        return chef;
    }

    public void setChef(User chef) {
        this.chef = chef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatEvent catEvent = (CatEvent) o;
        return id == catEvent.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    @Override
    public String toString() {
        return "CatEvent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chef=" + chef +
                ", tasks=" + tasks +
                '}';
    }

    public void deleteTask(Task task) {
        this.tasks.remove(task);
    }

    public int getId() {
        return this.id;
    }
}
