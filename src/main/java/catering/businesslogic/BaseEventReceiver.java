package catering.businesslogic;

public class BaseEventReceiver implements MenuEventReceiver {
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
}
