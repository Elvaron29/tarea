package com.sakila.data;

import java.util.List;

public interface IDataManager<T> {
    boolean post(T item);
    T get(int id);
    List<T> get();
    List<T> get(String searchTerm);
    boolean put(T item);
    boolean delete(int id);
}