package com.sakila.data;

import com.sakila.models.Language;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/** 
 * 
 * @author Nicolas De Jesus lopez Nuñez 
 * 
 * 
*/

public class LanguageManager implements IDataManager<Language> {
    private final Connection connection;
    private final ArrayList<Language> languages;

    public LanguageManager() {
        try {
            this.connection = DatabaseConnection.getConnection();
            this.languages = new ArrayList<>();
            loadLanguages();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener conexión", e);
        }
    }

    private void loadLanguages() {
        String sql = "SELECT * FROM language WHERE active = true ORDER BY name";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                languages.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar idiomas", e);
        }
    }

    @Override
    public boolean post(Language language) {
        String sql = "INSERT INTO language (name, active) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, language.getName());
            stmt.setBoolean(2, language.isActive());
            
            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        language.setLanguageId(rs.getInt(1));
                        languages.add(language);
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear idioma", e);
        }
    }

    @Override
    public Language get(int id) {
        return languages.stream()
                       .filter(l -> l.getLanguageId() == id)
                       .findFirst()
                       .orElse(null);
    }

    @Override
    public List<Language> get() {
        return new ArrayList<>(languages);
    }

    @Override
    public List<Language> get(String searchTerm) {
        return languages.stream()
                       .filter(l -> l.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                       .toList();
    }

    @Override
    public boolean put(Language language) {
        String sql = "UPDATE language SET name = ?, active = ? WHERE language_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, language.getName());
            stmt.setBoolean(2, language.isActive());
            stmt.setInt(3, language.getLanguageId());

            if (stmt.executeUpdate() > 0) {
                int index = languages.indexOf(get(language.getLanguageId()));
                if (index != -1) {
                    languages.set(index, language);
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar idioma", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE language SET active = false WHERE language_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            if (stmt.executeUpdate() > 0) {
                languages.removeIf(l -> l.getLanguageId() == id);
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar idioma", e);
        }
    }

    private Language mapResultSetToEntity(ResultSet rs) throws SQLException {
        Language language = new Language();
        language.setLanguageId(rs.getInt("language_id"));
        language.setName(rs.getString("name"));
        language.setLastUpdate(rs.getTimestamp("last_update"));
        language.setActive(rs.getBoolean("active"));
        return language;
    }
}