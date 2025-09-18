package com.example.taskmanager.dao;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDao {
    private final Path storagePath;
    private final Type listType = new TypeToken<List<Task>>(){}.getType();

    public TaskDao(Path storagePath) {
        this.storagePath = storagePath;
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath.getParent());
                saveAll(new ArrayList<>());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage file", e);
        }
    }

    public List<Task> findAll() {
        try {
            String json = Files.readString(storagePath, StandardCharsets.UTF_8);
            List<Task> list = JsonUtil.gson().fromJson(json, listType);
            return list == null ? new ArrayList<>() : list;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read tasks", e);
        }
    }

    public Optional<Task> findById(int id) {
        return findAll().stream().filter(t -> t.getId() == id).findFirst();
    }

    public Task save(Task task) {
        List<Task> list = findAll();
        boolean updated = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == task.getId()) {
                list.set(i, task);
                updated = true;
                break;
            }
        }
        if (!updated) list.add(task);
        saveAll(list);
        return task;
    }

    public boolean delete(int id) {
        List<Task> list = findAll();
        boolean removed = list.removeIf(t -> t.getId() == id);
        if (removed) saveAll(list);
        return removed;
    }

    private void saveAll(List<Task> list) {
        try {
            String json = JsonUtil.gson().toJson(list, listType);
            Files.writeString(storagePath, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save tasks", e);
        }
    }

    public int nextId() {
        return findAll().stream().mapToInt(Task::getId).max().orElse(0) + 1;
    }
}
