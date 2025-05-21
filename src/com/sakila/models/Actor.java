package com.sakila.models;

import java.sql.Timestamp;
/** 
 * 
 * @author Nicolas De Jesus lopez Nuñez 
 * 
 * 
*/

public class Actor {
    private int actorId;
    private String firstName;
    private String lastName;
    private Timestamp lastUpdate;

    public Actor() {}

    public Actor(int actorId, String firstName, String lastName, Timestamp lastUpdate) {
        this.actorId = actorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastUpdate = lastUpdate;
    }

    public int getActorId() { return actorId; }
    public void setActorId(int actorId) { this.actorId = actorId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Timestamp getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; }

    @Override
    public String toString() {
        return String.format("ID: %-4d | Nombre: %-15s | Apellido: %-15s", 
               actorId, firstName, lastName);
    }
}