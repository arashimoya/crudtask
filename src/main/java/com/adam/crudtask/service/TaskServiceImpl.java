package com.adam.crudtask.service;

import com.adam.crudtask.dao.TaskDAO;
import com.adam.crudtask.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{

    private TaskDAO taskDAO;

    @Autowired
    public TaskServiceImpl(TaskDAO taskDAO){
        this.taskDAO = taskDAO;
    }


    @Override
    @Transactional
    public List<Task> findAllTasks() {
        return taskDAO.findAllTasks();
    }

    @Override
    @Transactional
    public Task findById(int id) {
        return taskDAO.findById(id);
    }

    @Override
    @Transactional
    public void save(Task task) {
        taskDAO.save(task);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        taskDAO.deleteById(id);
    }
}
