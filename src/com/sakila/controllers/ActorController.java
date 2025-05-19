package com.sakila.controllers;

import com.sakila.models.Actor;
import com.sakila.models.ActorContext;
import java.util.List;
import java.util.Scanner;

public class ActorController {
    private final ActorContext context;
    private final Scanner scanner;

    public ActorController() {
        this.context = new ActorContext();
        this.scanner = new Scanner(System.in);
    }

    public void manageActors() {
        while (true) {
            System.out.println("\n=== GESTIÓN DE ACTORES ===");
            System.out.println("1. Listar todos los actores");
            System.out.println("2. Buscar actor por ID");
            System.out.println("3. Agregar nuevo actor");
            System.out.println("4. Actualizar actor");
            System.out.println("5. Eliminar actor");
            System.out.println("6. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> listActors();
                case 2 -> searchActorById();
                case 3 -> addActor();
                case 4 -> updateActor();
                case 5 -> deleteActor();
                case 6 -> { return; }
                default -> System.out.println("Opción no válida");
            }
        }
    }

    private void listActors() {
        List<Actor> actors = context.findAll();
        System.out.println("\n=== LISTA DE ACTORES ===");
        System.out.println("--------------------------------------------");
        actors.forEach(System.out::println);
        System.out.println("Total: " + actors.size() + " actores");
    }

    private void searchActorById() {
        System.out.print("\nIngrese el ID del actor: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Actor actor = context.findById(id);
        
        if (actor != null) {
            System.out.println("\nActor encontrado:");
            System.out.println(actor);
        } else {
            System.out.println("No se encontró un actor con ese ID");
        }
    }

    private void addActor() {
        System.out.println("\n=== AGREGAR NUEVO ACTOR ===");
        System.out.print("Nombre: ");
        String firstName = scanner.nextLine();
        System.out.print("Apellido: ");
        String lastName = scanner.nextLine();
        
        Actor actor = new Actor();
        actor.setFirstName(firstName);
        actor.setLastName(lastName);
        
        if (context.create(actor)) {
            System.out.println("Actor agregado exitosamente con ID: " + actor.getActorId());
        } else {
            System.out.println("Error al agregar el actor");
        }
    }

    private void updateActor() {
        System.out.print("\nIngrese el ID del actor a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Actor actor = context.findById(id);
        
        if (actor == null) {
            System.out.println("No se encontró un actor con ese ID");
            return;
        }
        
        System.out.println("Actor actual: " + actor);
        System.out.print("Nuevo nombre (" + actor.getFirstName() + "): ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) actor.setFirstName(firstName);
        
        System.out.print("Nuevo apellido (" + actor.getLastName() + "): ");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) actor.setLastName(lastName);
        
        if (context.update(actor)) {
            System.out.println("Actor actualizado exitosamente");
        } else {
            System.out.println("Error al actualizar el actor");
        }
    }

    private void deleteActor() {
        System.out.print("\nIngrese el ID del actor a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Actor actor = context.findById(id);
        if (actor == null) {
            System.out.println("No se encontró un actor con ese ID");
            return;
        }
        
        System.out.println("Está a punto de eliminar: " + actor);
        System.out.print("¿Está seguro? (s/n): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("s")) {
            if (context.remove(id)) {
                System.out.println("Actor eliminado exitosamente");
            } else {
                System.out.println("Error al eliminar el actor");
            }
        } else {
            System.out.println("Operación cancelada");
        }
    }
}