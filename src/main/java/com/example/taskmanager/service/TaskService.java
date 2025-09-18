package com.example.taskmanager.service;

import com.example.taskmanager.dao.TaskDao;
import com.example.taskmanager.model.Task;

import java.util.List;
import java.util.Optional;

public class TaskService {
    private final TaskDao dao;

    public TaskService(TaskDao dao) { this.dao = dao; }

    public List<Task> getAll() { return dao.findAll(); }

    public Task create(String title, String description) {
        int id = dao.nextId();
        Task t = new Task(id, title, description, false);
        return dao.save(t);
    }

    public Optional<Task> get(int id) { return dao.findById(id); }

    public Optional<Task> updateStatus(int id, boolean completed) {
        Optional<Task> ot = dao.findById(id);
        if (ot.isPresent()) {
            Task t = ot.get();
            t.setCompleted(completed);
            dao.save(t);
            return Optional.of(t);
        }
        return Optional.empty();
    }

    public boolean delete(int id) { return dao.delete(id); }
}
