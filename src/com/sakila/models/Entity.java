package com.sakila.models;

import java.sql.Timestamp;

public abstract class Entity {
    protected Timestamp lastUpdate;
    protected boolean active;

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}