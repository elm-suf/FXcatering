package catering.businesslogic.receivers;

import catering.businesslogic.grasp_controllers.Shift;
import catering.businesslogic.grasp_controllers.Task;
import catering.businesslogic.grasp_controllers.User;

public interface CatEventReceiver {
    void notifyTaskAdded(Task task);

    void notifyTaskRemoved(Task task);

    void notifyTaskSorted(Task task);

    void notifyTaskAssigned(Task task, Shift shift, User cook, String quantity, String duration, String difficulty);

    void notifyTaskAssignmentDeleted(Task task, User cook);
}
