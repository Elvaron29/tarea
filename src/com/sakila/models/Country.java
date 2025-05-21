package com.sakila.models;

public class Country extends Entity {
    private int countryId;
    private String country;

    public Country() {
        this.active = true;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return String.format("ID: %-4d | País: %-30s | Activo: %s", 
               countryId, country, active ? "Sí" : "No");
    }
}