package com.sakila.data;

import java.util.List;

public interface iDatapost<T> {
    boolean create(T item);
    T findById(int id);
    boolean update(T item);
    boolean remove(int id);
    List<T> findAll();
}