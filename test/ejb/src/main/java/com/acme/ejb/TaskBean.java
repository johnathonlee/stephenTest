package com.acme.ejb;

import com.acme.model.AcmeEntityManager;
import com.acme.model.Task;
import com.acme.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * User: dev
 * Date: 11/25/16.
 */
@Stateless
@Local(TaskService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class TaskBean implements TaskService {
    static final Logger logger = LoggerFactory.getLogger(TaskBean.class);

    @Inject
    @AcmeEntityManager
    EntityManager em;

    @Override
    public Task createTask() {
        Task task = new Task();
        em.persist(task);
        logger.trace("Created task with id {}", task.getId());
        return task;

    }


}
