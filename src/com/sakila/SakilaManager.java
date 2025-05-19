package com.sakila;

import com.sakila.controllers.ActorController;
import com.sakila.data.DatabaseConnection;
import java.sql.*;
import java.util.Scanner;

public class SakilaManager {
    public static void main(String[] args) {
        try {
            DatabaseConnection.getConnection();
            System.out.println("Conexión de la base de datos establecida correctamente");
        } catch (Exception e) {
            System.err.println("Error al conectar con la base de datos:");
            e.printStackTrace();
            return;
        }

        Scanner scanner = new Scanner(System.in);
        ActorController actorController = new ActorController();

        while (true) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Ver actores");
            System.out.println("2. Ver películas");
            System.out.println("3. Ver ciudades");
            System.out.println("4. Ver países");
            System.out.println("5. Ver calles (direcciones)");
            System.out.println("6. Ver películas rentadas (con año)");
            System.out.println("7. Ver ganancias totales por renta de películas");
            System.out.println("8. Ver suma total de todas las ganancias");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                int option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1 -> actorController.manageActors();
                    case 2 -> listFilms();
                    case 3 -> listCities();
                    case 4 -> listCountries();
                    case 5 -> listAddresses();
                    case 6 -> listRentedFilmsByYear();
                    case 7 -> showFilmRevenue();
                    case 8 -> showTotalRevenue();
                    case 9 -> {
                        DatabaseConnection.closeConnection();
                        System.out.println("Saliendo del sistema...");
                        System.exit(0);
                    }
                    default -> System.out.println("Opción no válida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese solo números");
            }
        }
    }

    private static void listFilms() {
        System.out.println("\n=== LISTADO DE PELÍCULAS ===");
        String sql = "SELECT film_id, title, release_year FROM film ORDER BY title LIMIT 50";
        executeAndPrintQuery(sql, "Películas");
    }

    private static void listCities() {
        System.out.println("\n=== LISTADO DE CIUDADES ===");
        String sql = "SELECT city_id, city, country_id FROM city ORDER BY city LIMIT 50";
        executeAndPrintQuery(sql, "Ciudades");
    }

    private static void listCountries() {
        System.out.println("\n=== LISTADO DE PAÍSES ===");
        String sql = "SELECT country_id, country FROM country ORDER BY country LIMIT 50";
        executeAndPrintQuery(sql, "Países");
    }

    private static void listAddresses() {
        System.out.println("\n=== LISTADO DE DIRECCIONES ===");
        String sql = "SELECT address_id, address, district, city_id FROM address ORDER BY address LIMIT 50";
        executeAndPrintQuery(sql, "Direcciones");
    }

    private static void listRentedFilmsByYear() {
        System.out.println("\n=== PELÍCULAS RENTADAS POR AÑO ===");
        String sql = """
            SELECT f.title, YEAR(r.rental_date) AS year, COUNT(*) AS rentals 
            FROM rental r
            JOIN inventory i ON r.inventory_id = i.inventory_id
            JOIN film f ON i.film_id = f.film_id
            GROUP BY f.title, YEAR(r.rental_date)
            ORDER BY year, rentals DESC
            LIMIT 50
            """;
        executeAndPrintQuery(sql, "Películas rentadas por año");
    }

    private static void showFilmRevenue() {
        System.out.println("\n=== GANANCIAS POR PELÍCULA ===");
        String sql = """
            SELECT f.title, SUM(p.amount) AS revenue 
            FROM payment p
            JOIN rental r ON p.rental_id = r.rental_id
            JOIN inventory i ON r.inventory_id = i.inventory_id
            JOIN film f ON i.film_id = f.film_id
            GROUP BY f.title
            ORDER BY revenue DESC
            LIMIT 20
            """;
        executeAndPrintQuery(sql, "Ganancias por película");
    }

    private static void showTotalRevenue() {
        System.out.println("\n=== GANANCIAS TOTALES ===");
        String sql = "SELECT SUM(amount) AS total FROM payment";
        executeAndPrintQuery(sql, "Ganancias totales");
    }

    private static void executeAndPrintQuery(String sql, String title) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Imprimir encabezados
            System.out.println("----------------------------------------------------");
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", metaData.getColumnName(i));
            }
            System.out.println("\n----------------------------------------------------");

            // Imprimir datos
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("%-20s", rs.getString(i));
                }
                System.out.println();
            }
            System.out.println("----------------------------------------------------");
        } catch (SQLException e) {
            System.err.println("Error al ejecutar consulta:");
            e.printStackTrace();
        }
    }
}