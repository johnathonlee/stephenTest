package com.acme.service;

import com.acme.model.Task;

import java.util.List;

/**
 * User: dev
 * Date: 11/25/16.
 */
public interface TaskGeneratorService {

    void generateTasks(int count);
    List<Task> getNotUpdatedTasks();
    void deleteTasks();
}
