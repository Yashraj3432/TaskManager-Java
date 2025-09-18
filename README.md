# TaskManager

A simple Java CLI task manager (Maven) that stores tasks in `data/tasks.json`.

## Build & Run

1. Make sure you have Java 17 (or above) and Maven installed.
2. From the project root run:

   mvn compile exec:java

3. The app will create `data/tasks.json` automatically and show a text menu.

## Notes for freshers

- `model` contains `Task` (POJO).
- `dao` handles persistence (file read/write using Gson).
- `service` contains business logic.
- `Main` is the CLI (user interaction).

