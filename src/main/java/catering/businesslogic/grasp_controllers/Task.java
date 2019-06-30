package catering.businesslogic.grasp_controllers;

import java.util.Objects;

public class Task {
    private int id;
    private Recipe recipe;
    private Shift shift;
    private User cook;
    private String quantity;
    private int difficulty;
    private Boolean isCompleted;
    private Boolean isAssigned;
    private String durationMinutes;
    private int index;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public User getCook() {
        return cook;
    }

    public void setCook(User cook) {
        this.cook = cook;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Task() {
    }

    public Task(int id, Recipe recipe, Shift shift, User cook, String quantity, int difficulty, boolean isCompleted, boolean isAssigned, String durationMinutes, int index) {
        this.id = id;
        this.recipe = recipe;
        this.shift = shift;
        this.cook = cook;
        this.quantity = quantity;
        this.difficulty = difficulty;
        this.isCompleted = isCompleted;
        this.isAssigned = isAssigned;
        this.durationMinutes = durationMinutes;
        this.index = index;
    }

    public Task(int id, Recipe recipe, String quantity, int difficulty, boolean isCompleted, boolean isAssigned, String durationMinutes, int index) {
        this.id = id;
        this.recipe = recipe;
        this.shift = null;
        this.cook = null;
        this.quantity = quantity;
        this.difficulty = difficulty;
        this.isCompleted = isCompleted;
        this.isAssigned = isAssigned;
        this.durationMinutes = durationMinutes;
        this.index = index;
    }

    public Task(Recipe recipe, User cook, Shift shift, boolean isCompleted, boolean isAssigned) {
        this.isCompleted = isCompleted;
        this.isAssigned = isAssigned;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public String getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(String durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getId() {
        return id;
    }


    public String toString() {
        return "Task{" +
                "id=" + id +
                ", index=" + index +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return getId() == task.getId() &&
                Objects.equals(getRecipe(), task.getRecipe());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRecipe());
    }
}
