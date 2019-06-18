package catering.businesslogic.managers;

import catering.businesslogic.exceptions.AssignTaskException;
import catering.businesslogic.exceptions.UseCaseLogicException;
import catering.businesslogic.grasp_controllers.CatEvent;
import catering.businesslogic.grasp_controllers.Recipe;
import catering.businesslogic.grasp_controllers.Task;
import catering.businesslogic.grasp_controllers.User;
import catering.businesslogic.receivers.CatEventReceiver;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private CatEvent currentEvent;

    private List<CatEventReceiver> receivers;

    public EventManager() {
        receivers = new ArrayList<>();

        receivers.add(new CatEventReceiver() {
            @Override
            public void notifyTaskAdded(Task task) {
                //todo
            }

            @Override
            public void notifyTaskRemoved(Task task) {
                //todo
            }

            @Override
            public void notifyTaskSorted(Task task) {
                //todo
            }

            @Override
            public void notifyTaskAssigned(Task task, User cook) {
                //todo
            }

            @Override
            public void notifyTaskAssignmentDeleted(Task task, User cook) {
                //todo
            }
        });
    }

    public void initialize() {
    }

    public void selectEvent(CatEvent event) throws AssignTaskException {
        User currentUser = checkIfChef();
        if (!event.getChef().equals(currentUser))
            throw new AssignTaskException("Solo lo chef incaricato dell'evento puo assegnare cose");
        currentEvent = event;
        //todo forse dobbiamo tornare qualcosa dopo eventuale Notify
    }


    public void addTask(Recipe recipe) throws AssignTaskException {
        User currentUser = checkIfChef();
        if (currentEvent == null) {
            throw new AssignTaskException("Evento non puo essere null");
        }
        if (!currentEvent.getChef().equals(currentUser)) {
            throw new AssignTaskException("Solo lo chef incaricato dell'evento puo assegnare cose");
        }

        Task task = new Task(recipe, null, null, false, false);
        currentEvent.addTask(task);

        receivers.forEach(rec -> rec.notifyTaskAdded(task));
    }

    public void deleteTAsk(Task task) throws AssignTaskException {
        User currentUser = checkIfChef();
        if (currentEvent == null) {
            throw new AssignTaskException("Evento non puo essere null");
        }
        if (!currentEvent.getChef().equals(currentUser)) {
            throw new AssignTaskException("Solo lo chef incaricato dell'evento puo assegnare cose");
        }
        if (!currentEvent.getTasks().contains(task))
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
        this.receivers.add(rec);
    }

}
