package catering.businesslogic.grasp_controllers;

public class Task {
    private MenuItem menuItem;
    private Shift shift;
    private User cook;
    private int quantity;
    private int difficulty;
    private boolean isCompleted;
    private boolean isAssigned;
    private int durationMinutes;
    private int index;

    public Task(MenuItem menuItem, Shift shift, User cook, int quantity, int difficulty, boolean isCompleted, boolean isAssigned, int durationMinutes, int index) {
        this.menuItem = menuItem;
        this.shift = shift;
        this.cook = cook;
        this.quantity = quantity;
        this.difficulty = difficulty;
        this.isCompleted = isCompleted;
        this.isAssigned = isAssigned;
        this.durationMinutes = durationMinutes;
        this.index = index;
    }

    public Task(MenuItem menuItem, User cook, Shift shift, boolean isCompleted, boolean isAssigned) {
        this.isCompleted = isCompleted;
        this.isAssigned = isAssigned;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isCompleted() {
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

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


}
