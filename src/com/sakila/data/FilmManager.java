package com.sakila.data;

import com.sakila.models.Film;
import com.sakila.models.Language;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmManager implements IDataManager<Film> {
    private final Connection connection;
    private final LanguageManager languageManager;
    private final ArrayList<Film> films;

    public FilmManager() {
        try {
            this.connection = DatabaseConnection.getConnection();
            this.languageManager = new LanguageManager();
            this.films = new ArrayList<>();
            loadFilms();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener conexión", e);
        }
    }

    private void loadFilms() {
        String sql = "SELECT * FROM film WHERE active = true ORDER BY title";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                films.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar películas", e);
        }
    }

    @Override
    public boolean post(Film film) {
        String sql = "INSERT INTO film (title, description, release_year, language_id, original_language_id, " +
                    "rental_duration, rental_rate, length, replacement_cost, rating, special_features, active) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, film.getTitle());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getReleaseYear());
            stmt.setInt(4, film.getLanguage().getLanguageId());
            stmt.setObject(5, film.getOriginalLanguage() != null ? 
                            film.getOriginalLanguage().getLanguageId() : null);
            stmt.setInt(6, film.getRentalDuration());
            stmt.setBigDecimal(7, film.getRentalRate());
            stmt.setInt(8, film.getLength());
            stmt.setBigDecimal(9, film.getReplacementCost());
            stmt.setString(10, film.getRating());
            stmt.setString(11, film.getSpecialFeatures());
            stmt.setBoolean(12, film.isActive());
            
            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        film.setFilmId(rs.getInt(1));
                        films.add(film);
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear película", e);
        }
    }

    @Override
    public Film get(int id) {
        return films.stream()
                   .filter(f -> f.getFilmId() == id)
                   .findFirst()
                   .orElse(null);
    }

    @Override
    public List<Film> get() {
        return new ArrayList<>(films);
    }

    @Override
    public List<Film> get(String searchTerm) {
        return films.stream()
                   .filter(f -> f.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
                   .toList();
    }

    @Override
    public boolean put(Film film) {
        String sql = "UPDATE film SET title = ?, description = ?, release_year = ?, language_id = ?, " +
                    "original_language_id = ?, rental_duration = ?, rental_rate = ?, length = ?, " +
                    "replacement_cost = ?, rating = ?, special_features = ?, active = ? " +
                    "WHERE film_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, film.getTitle());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getReleaseYear());
            stmt.setInt(4, film.getLanguage().getLanguageId());
            stmt.setObject(5, film.getOriginalLanguage() != null ? 
                            film.getOriginalLanguage().getLanguageId() : null);
            stmt.setInt(6, film.getRentalDuration());
            stmt.setBigDecimal(7, film.getRentalRate());
            stmt.setInt(8, film.getLength());
            stmt.setBigDecimal(9, film.getReplacementCost());
            stmt.setString(10, film.getRating());
            stmt.setString(11, film.getSpecialFeatures());
            stmt.setBoolean(12, film.isActive());
            stmt.setInt(13, film.getFilmId());

            if (stmt.executeUpdate() > 0) {
                int index = films.indexOf(get(film.getFilmId()));
                if (index != -1) {
                    films.set(index, film);
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar película", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE film SET active = false WHERE film_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            if (stmt.executeUpdate() > 0) {
                films.removeIf(f -> f.getFilmId() == id);
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar película", e);
        }
    }

    private Film mapResultSetToEntity(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setFilmId(rs.getInt("film_id"));
        film.setTitle(rs.getString("title"));
        film.setDescription(rs.getString("description"));
        film.setReleaseYear(rs.getInt("release_year"));
        
        Language language = languageManager.get(rs.getInt("language_id"));
        film.setLanguage(language);
        
        int originalLanguageId = rs.getInt("original_language_id");
        if (!rs.wasNull()) {
            Language originalLanguage = languageManager.get(originalLanguageId);
            film.setOriginalLanguage(originalLanguage);
        }
        
        film.setRentalDuration(rs.getInt("rental_duration"));
        film.setRentalRate(rs.getBigDecimal("rental_rate"));
        film.setLength(rs.getInt("length"));
        film.setReplacementCost(rs.getBigDecimal("replacement_cost"));
        film.setRating(rs.getString("rating"));
        film.setSpecialFeatures(rs.getString("special_features"));
        film.setLastUpdate(rs.getTimestamp("last_update"));
        film.setActive(rs.getBoolean("active"));
        
        return film;
    }
}