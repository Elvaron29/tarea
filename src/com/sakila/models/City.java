package com.sakila.models;


/** 
 * 
 * @author Nicolas De Jesus lopez Nuñez 
 * 
 * 
*/

public class City extends Entity {
    private int cityId;
    private String city;
    private Country country;

    public City() {
        this.active = true;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return String.format("ID: %-4d | Ciudad: %-30s | País: %-30s | Activo: %s", 
               cityId, city, country.getCountry(), active ? "Sí" : "No");
    }
}