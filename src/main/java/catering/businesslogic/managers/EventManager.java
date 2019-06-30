package catering.businesslogic.managers;

import catering.businesslogic.exceptions.AssignTaskException;
import catering.businesslogic.exceptions.UseCaseLogicException;
import catering.businesslogic.grasp_controllers.*;
import catering.businesslogic.receivers.BaseEventReceiver;
import catering.businesslogic.receivers.CatEventReceiver;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private CatEvent currentEvent;

    private List<CatEventReceiver> receivers;

    public EventManager() {
        receivers = new ArrayList<>();

        receivers.add(new BaseEventReceiver() {
            @Override
            public void notifyTaskAdded(Task task) {
                currentEvent.addTask(task);
            }

            @Override
            public void notifyTaskRemoved(Task task) {
                currentEvent.deleteTask(task);
            }
        });
    }

    public void initialize() {
    }

    public void selectEvent(CatEvent event) throws AssignTaskException {
        User currentUser = checkIfChef();
        if (!event.getChef().equals(currentUser))
            throw new AssignTaskException("Solo lo chef incaricato dell'evento puo assegnare cose");

        System.out.println("check: " + event.getChef() + " current : " + currentUser);
        System.out.println("ccc " + event.getChef().equals(currentUser));
        this.currentEvent = event;
        //todo forse dobbiamo tornare qualcosa dopo eventuale Notify

        receivers.forEach(rec -> rec.notifyEventSelected(currentEvent));
    }


    public void addTask(Recipe recipe) throws AssignTaskException {
        System.out.println("eventManager " + recipe.getId());
        System.out.println("eventManager  currentEvnt " + this.currentEvent);
        User currentUser = checkIfChef();
        if (currentEvent == null) {
            throw new AssignTaskException("Evento non puo essere null");
        }
        if (!currentEvent.getChef().equals(currentUser)) {
            throw new AssignTaskException("Solo lo chef incaricato dell'evento puo assegnare cose");
        }

        Task task = new Task();
        task.setRecipe(recipe);
        task.setAssigned(false);
        task.setCompleted(false);
        task.setId(CateringAppManager.dataManager.addTask(recipe));
        currentEvent.addTask(task);

        System.out.println("sending " + task.getRecipe());
        System.out.println("sen " + recipe);
        receivers.forEach(rec -> rec.notifyTaskAdded(task));
    }

    public void addTasks() throws AssignTaskException {
        User currentUser = checkIfChef();
        if (currentEvent == null) {
            throw new AssignTaskException("Evento non può essere null");
        }
        if (!currentEvent.getChef().equals(currentUser)) {
            throw new AssignTaskException("Solo lo chef incaricato dell'evento puo assegnare cose");
        }
        System.out.println("currentEvent: " + currentEvent.getId());
        List<Recipe> recipes = CateringAppManager.dataManager.loadRecipesFromEvent(currentEvent);
        for (Recipe recipe : recipes) {
            System.out.println(recipe);
            Task task = new Task();
            task.setRecipe(recipe);
            task.setAssigned(false);
            task.setCompleted(false);
            task.setId(CateringAppManager.dataManager.addTask(recipe));
            currentEvent.addTask(task);
            receivers.forEach(rec -> rec.notifyTaskAdded(task));
        }

    }

    public void deleteTAsk(Task task) throws AssignTaskException {
        System.out.println("DeleteTask " + task);
        User currentUser = checkIfChef();
        if (currentEvent == null) {
            throw new AssignTaskException("Evento non puo essere null");
        }
        if (!currentEvent.getChef().equals(currentUser)) {
            throw new AssignTaskException("Solo lo chef incaricato dell'evento puo assegnare cose");
        }
        if (!getAllTasks().contains(task))
            throw new AssignTaskException("Non puoi eliminare Task inesistente");

        currentEvent.deleteTask(task);

        receivers.forEach(rec -> rec.notifyTaskRemoved(task));
    }


    //===================================================================================\\
    private User checkIfChef() {
        User currentUser = CateringAppManager.userManager.getCurrentUser();
        if (!currentUser.isChef())
            throw new UseCaseLogicException("Solo gli chef possono assegnare compiti");

        return currentUser;
    }

    public void addReceiver(CatEventReceiver rec) {
        System.out.println("~~~~~~~~~~~~~adding receiver " + rec.toString());
        this.receivers.add(rec);
    }

    public List<CatEvent> getAllEvents(User currentUser) {
        return CateringAppManager.dataManager.loadEvents(currentUser);
    }


    public List<Task> getAllTasks() {
        return CateringAppManager.dataManager.loadTasks(currentEvent);
    }

    public void assignTask(Task task, Shift shift, User cook, String quantity, String duration, String difficulty) {
        task.setShift(shift);
        task.setCook(cook);
        task.setQuantity(quantity);
        task.setDurationMinutes(duration);
        switch (difficulty) {
            case "Facile":
                task.setDifficulty(0);
                break;
            case "Medio":
                task.setDifficulty(1);
                break;
            default:
                task.setDifficulty(2);
                break;
        }

        for (CatEventReceiver r : receivers) {
            r.notifyTaskAssigned(task);
        }
    }

    public CatEvent getCurrentEvent() {
        return currentEvent;
    }

    public void sortTask(Task task, int newIndex) {
        //todo check user
        CateringAppManager.dataManager.sortTask(task, newIndex);
        currentEvent.deleteTask(task);
        task.setIndex(newIndex);
        currentEvent.addTask(task);
        receivers.forEach(rec -> rec.notifyTaskSorted(task));
    }

    public void removeReceiver(CatEventReceiver receiver) {
        receivers.remove(receiver);
    }
}
