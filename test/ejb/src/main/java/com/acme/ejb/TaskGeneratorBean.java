package com.acme.ejb;

import com.acme.common.annotations.TaskQueue;
import com.acme.model.Task;
import com.acme.service.TaskGeneratorService;
import com.acme.service.TaskService;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * User: dev
 * Date: 11/25/16.
 */
@Stateless
@Remote(TaskGeneratorService.class)
public class TaskGeneratorBean implements TaskGeneratorService{
    static final Logger logger = LoggerFactory.getLogger(TaskGeneratorBean.class);
    @EJB
    private TaskService taskService;

    @PersistenceContext
    private EntityManager em;

    @Inject
    @TaskQueue
    QueueSender sender;

    @Inject
    @TaskQueue
    QueueSession session;

    @Override
    public void deleteTasks(){
        em.createQuery("delete from Task t").executeUpdate();
    }


    @Override
    public List<Task> getNotUpdatedTasks(){
        return em.createQuery("select t from Task t where t.index = 0", Task.class).getResultList();

    }

    @Override
    public void generateTasks(int count) {
        for (int i = 0; i<count; i++){
            Task task = taskService.createTask();
            putTaskOnQueue(task);
        }
    }


    protected void putTaskOnQueue(Task task) {
        if (task == null) {
            return;
        }
        String messageBody = task.getId().toString();
        try {
            TextMessage tm = session.createTextMessage(messageBody);
            sender.send(tm);
            logger.trace("Sent event {}", messageBody);
        } catch (JMSException e) {
            logger.warn("Unable to send JMS message", e);
        }


    }
}
