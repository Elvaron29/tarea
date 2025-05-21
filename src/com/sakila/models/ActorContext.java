package com.sakila.models;

import com.sakila.data.DataContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/** 
 * 
 * @author Nicolas De Jesus lopez Nuñez 
 * 
 * 
*/


public class ActorContext extends DataContext<Actor> {

    @Override
    protected Actor mapResultSetToEntity(ResultSet rs) throws SQLException {
        Actor actor = new Actor();
        actor.setActorId(rs.getInt("actor_id"));
        actor.setFirstName(rs.getString("first_name"));
        actor.setLastName(rs.getString("last_name"));
        actor.setLastUpdate(rs.getTimestamp("last_update"));
        return actor;
    }

    @Override
    public boolean create(Actor actor) {
        String sql = "INSERT INTO actor (first_name, last_name) VALUES (?, ?)";
        try (PreparedStatement stmt = prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, actor.getFirstName());
            stmt.setString(2, actor.getLastName());
            
            if (stmt.executeUpdate() == 0) return false;
            
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    actor.setActorId(keys.getInt(1));
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear actor", e);
        }
    }

    @Override
    public Actor findById(int id) {
        String sql = "SELECT * FROM actor WHERE actor_id = ?";
        try (PreparedStatement stmt = prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToEntity(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar actor", e);
        }
    }

    @Override
    public boolean update(Actor actor) {
        String sql = "UPDATE actor SET first_name = ?, last_name = ? WHERE actor_id = ?";
        try (PreparedStatement stmt = prepareStatement(sql)) {
            stmt.setString(1, actor.getFirstName());
            stmt.setString(2, actor.getLastName());
            stmt.setInt(3, actor.getActorId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar actor", e);
        }
    }

    @Override
    public boolean remove(int id) {
        String sql = "DELETE FROM actor WHERE actor_id = ?";
        try (PreparedStatement stmt = prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar actor", e);
        }
    }

    @Override
    public List<Actor> findAll() {
        List<Actor> actors = new ArrayList<>();
        String sql = "SELECT * FROM actor ORDER BY last_name, first_name";
        try (PreparedStatement stmt = prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                actors.add(mapResultSetToEntity(rs));
            }
            return actors;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener actores", e);
        }
    }
}