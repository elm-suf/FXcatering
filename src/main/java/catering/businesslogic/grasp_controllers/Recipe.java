package catering.businesslogic.grasp_controllers;

import java.util.Objects;

// questa classe è solo uno scheletro
// sono implementate solo le caratteristiche richieste da "Gestire menu"
// E' possibile modificarne senza conseguenze l'implementazione interna, ma se si  vuole
// che il SW continui a funzionare non è il caso di cambiarne l'interfaccia (per cambiare
// si intende modificare quel che già c'è, aggiungere è sempre possibile)
public class Recipe {
    public String getName() {
        return name;
    }

    private int id;

    public enum Type {Preparation, Dish}

    private String name;
    private Type type;

    public Recipe(int id, String name, Type t) {
        this.name = name;
        this.type = t;
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public boolean isDish() {
        return this.type.equals(Type.Dish);
    }

    public boolean isPreparation() {
        return this.type.equals(Type.Preparation);
    }

    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return getId() == recipe.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
