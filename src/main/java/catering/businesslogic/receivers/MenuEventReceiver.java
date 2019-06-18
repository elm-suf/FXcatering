package catering.businesslogic.receivers;

import catering.businesslogic.grasp_controllers.Menu;
import catering.businesslogic.grasp_controllers.MenuItem;
import catering.businesslogic.grasp_controllers.Section;

public interface MenuEventReceiver {
    void notifyMenuCreated(Menu m);

    void notifySectionAdded(Menu m, Section s);

    void notifyItemAdded(Menu m, Section s, MenuItem it);

    void notifyMenuPublished(Menu m);

    void notifyMenuDeleted(Menu m);

    void notifySectionRemoved(Menu m, Section s);

    void notifySectionNameChanged(Menu m, Section s);

    void notifySectionsRearranged(Menu m);

    void notifyItemsRearranged(Menu m, Section s);

    void notifyItemsRearrangedInMenu(Menu m);

    void notifyItemMoved(Menu m, Section oldS, Section newS, MenuItem it);

    void notifyItemDescriptionChanged(Menu m, MenuItem it);

    void notifyItemDeleted(Menu m, MenuItem it);

    void notifyMenuTitleChanged(Menu m);
}
