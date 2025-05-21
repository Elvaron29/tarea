package com.sakila.models;

/** 
 * 
 * @author Nicolas De Jesus lopez Nuñez 
 * 
 * 
*/
public class Language extends Entity {
    private int languageId;
    private String name;

    public Language() {
        this.active = true;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("ID: %-4d | Idioma: %-20s | Activo: %s",
                languageId, name, active ? "Sí" : "No");
    }
}