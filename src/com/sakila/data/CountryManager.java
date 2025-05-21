package com.sakila.data;

import com.sakila.models.Country;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CountryManager implements IDataManager<Country> {
    private final Connection connection;

    public CountryManager() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener conexión", e);
        }
    }

    @Override
    public boolean post(Country country) {
        String sql = "INSERT INTO country (country, active) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, country.getCountry());
            stmt.setBoolean(2, country.isActive());
            
            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        country.setCountryId(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear país", e);
        }
    }

    @Override
    public Country get(int id) {
        String sql = "SELECT * FROM country WHERE country_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar país", e);
        }
        return null;
    }

    @Override
    public List<Country> get() {
        List<Country> countries = new ArrayList<>();
        String sql = "SELECT * FROM country ORDER BY country";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                countries.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener países", e);
        }
        return countries;
    }

    @Override
    public List<Country> get(String searchTerm) {
        List<Country> countries = new ArrayList<>();
        String sql = "SELECT * FROM country WHERE country LIKE ? ORDER BY country";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    countries.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar países", e);
        }
        return countries;
    }

    @Override
    public boolean put(Country country) {
        String sql = "UPDATE country SET country = ?, active = ? WHERE country_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, country.getCountry());
            stmt.setBoolean(2, country.isActive());
            stmt.setInt(3, country.getCountryId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar país", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE country SET active = false WHERE country_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar país", e);
        }
    }

    private Country mapResultSetToEntity(ResultSet rs) throws SQLException {
        Country country = new Country();
        country.setCountryId(rs.getInt("country_id"));
        country.setCountry(rs.getString("country"));
        country.setLastUpdate(rs.getTimestamp("last_update"));
        country.setActive(rs.getBoolean("active"));
        return country;
    }
}