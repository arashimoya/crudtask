package com.adam.crudtask;

import com.adam.crudtask.dao.TaskDAO;
import com.adam.crudtask.entity.Task;
import com.adam.crudtask.rest.TaskRESTController;
import com.adam.crudtask.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskRESTController.class)
public class TaskRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    TaskDAO taskDAO;

    @MockBean
    TaskService taskService;

    Task TASK_1 = new Task(11, "task_title1","task_desc1",true );
    Task TASK_2 = new Task(22, "task_title2","task_desc2",true );
    Task TASK_3 = new Task(33, "task_title3","task_desc3",true );

    @Test
    public void getAllTasks_success() throws Exception {
        List<Task> tasks = new ArrayList<>(Arrays.asList(TASK_1, TASK_2, TASK_3));

        Mockito.when(taskService.findAllTasks()).thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders
                .get("http://localhost:2137/api/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].title", is("task_title3")));
    }

    @Test
    public void getTaskById_success() throws Exception {
        Mockito.when(taskService.findById(TASK_1.getId())).thenReturn(TASK_1);


        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/tasks/11")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.title", is(TASK_1.getTitle())));
    }

    @Test
    public void postTask_success () throws Exception {
        Task task = Task.builder()
                .title("Lorem Ipsum")
                .description("dolor sit amet")
                .completed(true)
                .build();

        Mockito.when(taskService.save(task)).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.title", is("Lorem Ipsum")));
    }

    @Test
    public void updateTask_nullId ()throws Exception{
        Task updatedTask = Task.builder()
                .title("Lorem Ipsum")
                .description("dolor sit amet")
                .completed(true)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedTask)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result ->
                        assertEquals("400 BAD_REQUEST \"Task or ID must not be null!\"",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));


    }
    @Test
    public void updateTask_taskNotFound ()throws Exception{
        Task updatedTask = Task.builder()
                .id(999)
                .title("Lorem Ipsum")
                .description("dolor sit amet")
                .completed(true)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(updatedTask)))

                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result ->
                        assertEquals("404 NOT_FOUND \"Task with ID 999 does not exist.\"",
                                Objects.requireNonNull(result.getResolvedException()).getMessage() ));
    }

    @Test
    public void updateTask_success() throws Exception{
        Task updatedTask = Task.builder()
                .id(11)
                .title("Lorem Ipsum")
                .description("dolor sit amet")
                .completed(true)
                .build();

        Mockito.when(taskService.findById(TASK_1.getId())).thenReturn(TASK_1);
        Mockito.when(taskService.save(updatedTask)).thenReturn(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.title", is("Lorem Ipsum")));
    }

    @Test
    public void deleteTaskById_Success() throws Exception {
        Mockito.when(taskService.findById(TASK_2.getId())).thenReturn(TASK_2);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/tasks/22")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void deleteTaskById_notFound() throws Exception{
        Mockito.when(taskService.findById(999)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/tasks/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result ->
                        assertEquals("404 NOT_FOUND \"Task with ID 999 does not exist.\"",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }








}
