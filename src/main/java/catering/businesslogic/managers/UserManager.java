package catering.businesslogic.managers;

import catering.businesslogic.grasp_controllers.Shift;
import catering.businesslogic.grasp_controllers.User;

import java.util.List;

public class UserManager {

    private User currentUser;

    public void initialize() {
        // Questa è una versione "mockup" di UserManager
        // Ossia una versione semplificata che ha lo scopo di testare l'applicazione
        // Per questa ragione il metodo initialize() carica un utente di default dal DB
        this.currentUser = CateringAppManager.dataManager.loadUser("Viola");
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<User> getUsersInShift(Shift shift) {
        return CateringAppManager.dataManager.loadUsersInShift(shift);
    }
}
