package catering.businesslogic.managers;

import catering.businesslogic.grasp_controllers.Shift;

import java.util.ArrayList;
import java.util.List;

public class ShiftManager {

    private List<Shift> shifts;

    public ShiftManager() {
    }

    public List<Shift> getShifts() {
        if (shifts == null) {
            shifts = new ArrayList<>();
            this.shifts.addAll(CateringAppManager.dataManager.getShifts());
        }

        // Restituisce una copia della propria lista per impedire ad altri oggetti di modificarne
        // il contenuto
        return new ArrayList<>(shifts);
    }
}
