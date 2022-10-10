package com.adam.crudtask.service;

import com.adam.crudtask.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAllTasks();

    Task findById(int id);

    Task save(Task task);

    void deleteById(int id);
}
