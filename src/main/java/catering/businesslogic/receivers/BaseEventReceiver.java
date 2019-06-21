package catering.businesslogic.receivers;

import catering.businesslogic.grasp_controllers.*;

public class BaseEventReceiver implements MenuEventReceiver, CatEventReceiver {
    @Override
    public void notifyMenuCreated(Menu m) {
    }

    @Override
    public void notifySectionAdded(Menu m, Section s) {

    }

    @Override
    public void notifyItemAdded(Menu m, Section s, MenuItem it) {

    }

    @Override
    public void notifyMenuPublished(Menu m) {

    }

    @Override
    public void notifyMenuDeleted(Menu m) {

    }

    @Override
    public void notifySectionRemoved(Menu m, Section s) {

    }

    @Override
    public void notifySectionNameChanged(Menu m, Section s) {

    }

    @Override
    public void notifySectionsRearranged(Menu m) {

    }

    @Override
    public void notifyItemsRearranged(Menu m, Section s) {

    }

    @Override
    public void notifyItemsRearrangedInMenu(Menu m) {

    }

    @Override
    public void notifyItemMoved(Menu m, Section oldS, Section newS, MenuItem it) {

    }

    @Override
    public void notifyItemDescriptionChanged(Menu m, MenuItem it) {

    }

    @Override
    public void notifyItemDeleted(Menu m, MenuItem it) {

    }

    @Override
    public void notifyMenuTitleChanged(Menu m) {

    }


    /////////////////////////////////////////////////CAT EVENT RECEIVERS


    @Override
    public void notifyTaskAdded(Task task) {

    }

    @Override
    public void notifyTaskRemoved(Task task) {

    }

    @Override
    public void notifyTaskSorted(Task task) {

    }

    @Override
    public void notifyTaskAssigned(Task task) {

    }

    @Override
    public void notifyTaskAssignmentDeleted(Task task, User cook) {

    }

    @Override
    public void notifyEventSelected(CatEvent event) {

    }
}
