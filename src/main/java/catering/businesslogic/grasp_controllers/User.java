package catering.businesslogic.grasp_controllers;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String name;
    private Set<Role> roles;
    public User(String name) {
        this.name = name;
        this.roles = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public boolean isCook() {
        return this.roles.contains(Role.Cuoco);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public boolean isChef() {
        return this.roles.contains(Role.Chef);
    }

    public enum Role {Cuoco, Chef, Organizzatore, Servizio}

    public String toString() {
        return this.name;
    }
}
