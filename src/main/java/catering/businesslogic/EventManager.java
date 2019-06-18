package catering.businesslogic;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private CatEvent currentEvent;

    private List<CatEventReceivers> receivers;

    public EventManager() {
        receivers = new ArrayList<>();

//        receivers.add(new BaseEventReceiver() {
//            @Override
//            public void notifyMenuCreated(Menu m) {
//                allMenus.add(m);
//            }
//
//            @Override
//            public void notifyMenuDeleted(Menu m) {
//                allMenus.remove(m);
//            }
//        });
    };

    // Nota: nell'inizializzazione non carichiamo l'elenco di ricette
    // perché lo faremo "onDemand", ossia se viene richiesto da qualche altro oggetto
    // L'idea è evitare di caricare tutto se non serve.
    public void initialize() {};




}
