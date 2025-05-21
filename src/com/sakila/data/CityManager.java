package com.sakila.data;

import com.sakila.models.City;
import com.sakila.models.Country;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/** 
 * 
 * @author Nicolas De Jesus lopez Nuñez 
 * 
 * 
*/

public class CityManager implements IDataManager<City> {
    private final Connection connection;
    private final CountryManager countryManager;

    public CityManager() {
        try {
            this.connection = DatabaseConnection.getConnection();
            this.countryManager = new CountryManager();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener conexión", e);
        }
    }

    @Override
    public boolean post(City city) {
        String sql = "INSERT INTO city (city, country_id, active) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, city.getCity());
            stmt.setInt(2, city.getCountry().getCountryId());
            stmt.setBoolean(3, city.isActive());
            
            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        city.setCityId(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear ciudad", e);
        }
    }

    @Override
    public City get(int id) {
        String sql = "SELECT * FROM city WHERE city_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ciudad", e);
        }
        return null;
    }

    @Override
    public List<City> get() {
        List<City> cities = new ArrayList<>();
        String sql = "SELECT * FROM city ORDER BY city";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cities.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener ciudades", e);
        }
        return cities;
    }

    @Override
    public List<City> get(String searchTerm) {
        List<City> cities = new ArrayList<>();
        String sql = "SELECT * FROM city WHERE city LIKE ? ORDER BY city";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cities.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ciudades", e);
        }
        return cities;
    }

    @Override
    public boolean put(City city) {
        String sql = "UPDATE city SET city = ?, country_id = ?, active = ? WHERE city_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, city.getCity());
            stmt.setInt(2, city.getCountry().getCountryId());
            stmt.setBoolean(3, city.isActive());
            stmt.setInt(4, city.getCityId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar ciudad", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE city SET active = false WHERE city_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar ciudad", e);
        }
    }

    private City mapResultSetToEntity(ResultSet rs) throws SQLException {
        City city = new City();
        city.setCityId(rs.getInt("city_id"));
        city.setCity(rs.getString("city"));
        city.setLastUpdate(rs.getTimestamp("last_update"));
        city.setActive(rs.getBoolean("active"));
        
        // Cargar el país asociado
        Country country = countryManager.get(rs.getInt("country_id"));
        city.setCountry(country);
        
        return city;
    }
}