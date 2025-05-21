package com.sakila.data;

import java.sql.*;
import java.util.List;

/** 
 * 
 * @author Nicolas De Jesus lopez Nuñez 
 * 
 * 
*/


public abstract class DataContext<T> implements iDatapost<T> {
    protected Connection connection;

    public DataContext() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener conexión a la base de datos", e);
        }
    }

    @Override
    public abstract boolean create(T item);
    @Override
    public abstract T findById(int id);
    @Override
    public abstract boolean update(T item);
    @Override
    public abstract boolean remove(int id);
    @Override
    public abstract List<T> findAll();

    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected final PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    protected final PreparedStatement prepareStatement(String sql, int flags) throws SQLException {
        return connection.prepareStatement(sql, flags);
    }

    protected final void closeResources(ResultSet rs, PreparedStatement stmt) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos:");
            e.printStackTrace();
        }
    }
}