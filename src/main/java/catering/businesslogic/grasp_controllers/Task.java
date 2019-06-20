package catering.businesslogic.grasp_controllers;

public class Task {
    private int id;
    private Recipe recipe;
    private Shift shift;
    private User cook;
    private int quantity;
    private int difficulty;
    private Boolean isCompleted;
    private Boolean isAssigned;
    private int durationMinutes;
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

    public Task() {
    }

    public Task(int id, Recipe recipe, Shift shift, User cook, int quantity, int difficulty, boolean isCompleted, boolean isAssigned, int durationMinutes, int index) {
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

    public Task(int id, Recipe recipe, int quantity, int difficulty, boolean isCompleted, boolean isAssigned, int durationMinutes, int index) {
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

    public int getId() {
        return id;
    }


    @Override
    public String toString() {
        return "Task{" +
                "recipe=" + recipe.getName() +
                ", shift=" + shift +
                ", cook=" + cook.getName() +
                ", isCompleted=" + isCompleted +
                ", isAssigned=" + isAssigned +
                ", durationMinutes=" + durationMinutes +
                ", index=" + index +
                '}';
    }
}
