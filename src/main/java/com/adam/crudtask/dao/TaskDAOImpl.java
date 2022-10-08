package com.adam.crudtask.dao;

import com.adam.crudtask.entity.Task;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import org.hibernate.query.Query;

import java.util.List;

@Repository
public class TaskDAOImpl implements TaskDAO{

    //define field for entitymanager
    private EntityManager entityManager;

    //set up constructor injection
    @Autowired
    public TaskDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<Task> findAllTasks() {

        //get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);
        //create query
        Query<Task> theQuery = currentSession.createQuery("FROM Task");

        //execute query and get the result list
        List<Task> tasks = theQuery.getResultList();
        //return the results
        return tasks;
    }

    @Override
    public Task findById(int id) {
        //get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);
        //get the task
        Task task = currentSession.get(Task.class, id);

        //return the task
        return task;
    }

    @Override
    public void save(Task task) {
        //get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);
        //save task
        currentSession.saveOrUpdate(task);
    }

    @Override
    public void deleteById(int id) {
        //get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);
        //delete task
        Query theQuery = currentSession.createQuery(
                "delete FROM Task where id=:taskId");

        theQuery.setParameter("taskId", id);
        theQuery.executeUpdate();
    }
}
