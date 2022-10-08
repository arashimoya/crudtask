package com.adam.crudtask.dao;

import com.adam.crudtask.entity.Task;

import java.util.List;

public interface TaskDAO {

    List<Task> findAllTasks();
    Task findById(int id);
    void save(Task task);
    void deleteById(int id);


}
