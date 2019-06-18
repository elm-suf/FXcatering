package catering.businesslogic.receivers;

import catering.businesslogic.grasp_controllers.Task;
import catering.businesslogic.grasp_controllers.User;

public interface CatEventReceiver {
    void notifyTaskAdded(Task task);

    void notifyTaskRemoved(Task task);

    void notifyTaskSorted(Task task);

    void notifyTaskAssigned(Task task, User cook);

    void notifyTaskAssignmentDeleted(Task task, User cook);
}
