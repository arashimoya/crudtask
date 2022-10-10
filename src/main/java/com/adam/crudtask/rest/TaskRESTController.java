package com.adam.crudtask.rest;

import com.adam.crudtask.dao.TaskDAO;
import com.adam.crudtask.entity.Task;
import com.adam.crudtask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Task id not found - " + taskId);
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
        if(task==null || task.getId()==0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Task or ID must not be null!");
        }

        Optional<Task> optionalTask = Optional.ofNullable(taskService.findById(task.getId()));
        if(optionalTask.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + task.getId() + " does not exist.");
        }
        taskService.save(task);
        return task;
    }

    //add mapping for DELETE /tasks/{taskId} - delete task
    @DeleteMapping("/tasks/{taskId}")
    public String deleteTask(@PathVariable int taskId){
        Task tempTask = taskService.findById(taskId);

        if(tempTask==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Task with ID " + taskId + " does not exist.");
        }

        taskService.deleteById(taskId);
        return "Deleted task by id - " + taskId;
    }

}
