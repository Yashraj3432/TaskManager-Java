package com.example.taskmanager;

import com.example.taskmanager.dao.TaskDao;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Path DEFAULT_STORAGE = Path.of("data", "tasks.json");

    public static void main(String[] args) {
        TaskDao dao = new TaskDao(DEFAULT_STORAGE);
        TaskService service = new TaskService(dao);
        Scanner sc = new Scanner(System.in);

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Choose option: ");
            String input = sc.nextLine().trim();
            switch (input) {
                case "1" -> createTask(sc, service);
                case "2" -> listTasks(service);
                case "3" -> updateTask(sc, service);
                case "4" -> deleteTask(sc, service);
                case "0" -> {
                    running = false;
                    System.out.println("Exiting. Bye!");
                }
                default -> System.out.println("Invalid option. Try again.");
            }
            System.out.println();
        }
        sc.close();
    }

    private static void printMenu() {
        System.out.println("==== Task Manager ====");
        System.out.println("1. Create task");
        System.out.println("2. List tasks");
        System.out.println("3. Mark complete / incomplete");
        System.out.println("4. Delete task");
        System.out.println("0. Exit");
    }

    private static void createTask(Scanner sc, TaskService service) {
        System.out.print("Title: ");
        String title = sc.nextLine().trim();
        System.out.print("Description: ");
        String desc = sc.nextLine().trim();
        Task t = service.create(title, desc);
        System.out.println("Created: " + t);
    }

    private static void listTasks(TaskService service) {
        List<Task> list = service.getAll();
        if (list.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        list.forEach(System.out::println);
    }

    private static void updateTask(Scanner sc, TaskService service) {
        System.out.print("Enter task id: ");
        String idS = sc.nextLine().trim();
        try {
            int id = Integer.parseInt(idS);
            Optional<Task> ot = service.get(id);
            if (ot.isEmpty()) {
                System.out.println("Task not found.");
                return;
            }
            System.out.print("Mark completed? (y/n): ");
            String ans = sc.nextLine().trim().toLowerCase();
            boolean completed = ans.equals("y") || ans.equals("yes");
            Optional<Task> updated = service.updateStatus(id, completed);
            updated.ifPresent(task -> System.out.println("Updated: " + task));
        } catch (NumberFormatException e) {
            System.out.println("Invalid id.");
        }
    }

    private static void deleteTask(Scanner sc, TaskService service) {
        System.out.print("Enter task id to delete: ");
        String idS = sc.nextLine().trim();
        try {
            int id = Integer.parseInt(idS);
            boolean ok = service.delete(id);
            System.out.println(ok ? "Deleted." : "Task not found.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid id.");
        }
    }
}
