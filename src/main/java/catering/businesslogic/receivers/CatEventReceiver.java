package catering.businesslogic.receivers;

import catering.businesslogic.grasp_controllers.CatEvent;
import catering.businesslogic.grasp_controllers.Task;
import catering.businesslogic.grasp_controllers.User;

public interface CatEventReceiver {
    void notifyTaskAdded(Task task);

    void notifyTaskRemoved(Task task);

    void notifyTaskSorted(Task task);

    void notifyTaskAssigned(Task task);

    void notifyTaskAssignmentDeleted(Task task, User cook);

    void notifyEventSelected(CatEvent event);
}
