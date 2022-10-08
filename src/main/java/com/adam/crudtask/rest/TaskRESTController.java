package com.adam.crudtask.rest;

import com.adam.crudtask.dao.TaskDAO;
import com.adam.crudtask.entity.Task;
import com.adam.crudtask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskRESTController {

    private TaskService taskService;

    @Autowired
    public TaskRESTController(TaskService taskService){
        this.taskService = taskService;
    }

    //expose "/tasks" and return list of tasks
    @GetMapping("/tasks")
    public List<Task> findAll(){
        return taskService.findAllTasks();
    }

    //add mapping for GET /tasks/{taskId}
    @GetMapping("/tasks/{taskId}")
    public Task getTask(@PathVariable int taskId){
        Task task = taskService.findById(taskId);
        if(task==null){
            throw new RuntimeException("Employee id not found - " + taskId);
        }
        return task;
    }
    //add mapping for POST /tasks
    @PostMapping("/tasks")
    public Task addTask(@RequestBody Task task){

        task.setId(0);

        taskService.save(task);

        return task;
    }

    //add mapping for PUT /tasks - update existing task
    @PutMapping("/tasks")
    public Task updateTask(@RequestBody Task task){
        taskService.save(task);
        return task;
    }

    //add mapping for DELETE /tasks/{taskId} - delete task
    @DeleteMapping("/tasks/{taskId}")
    public String deleteTask(@PathVariable int taskId){
        Task tempTask = taskService.findById(taskId);

        if(tempTask==null){
            throw new RuntimeException("Task ID not found - " + taskId);
        }

        taskService.deleteById(taskId);
        return "Deleted task by id - " + taskId;
    }

}
