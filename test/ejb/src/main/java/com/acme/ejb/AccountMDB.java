package com.acme.ejb;

import com.acme.common.annotations.TaskQueue;
import com.acme.model.Account;
import com.acme.model.AcmeEntityManager;
import com.acme.model.Task;
import com.acme.service.TaskService;
import org.jboss.ejb3.annotation.ResourceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.*;
import javax.persistence.EntityManager;

/**
 * User: dev
 * Date: 11/25/16.
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/topic/lifecycle"),
        @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "action='UPDATE' AND type='com.acme.model.Account' "),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "50")
})
@ResourceAdapter(value = "activemq-ra")
public class AccountMDB implements MessageListener {

    @EJB
    private TaskService taskService;

    @Inject
    @TaskQueue
    QueueSender sender;

    @Inject
    @TaskQueue
    QueueSession session;

    @Inject
    @AcmeEntityManager
    EntityManager em;

    static final Logger logger = LoggerFactory.getLogger(AccountMDB.class);


    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                TextMessage text = (TextMessage) message;
                Long id = Long.parseLong(text.getText());
                Account account = em.find(Account.class, id);
                logger.trace("Account {} updated with id {}", account, id);

                Task task = taskService.createTask();
                putTaskOnQueue(task);
logger.info("Sleeping 10 seconds");
Thread.sleep(10000);
logger.info("Awake after 10 second sleep");
            } catch (Exception e) {
                logger.warn("Unable to process message", e);
            }

        } else {
            logger.info("Received message is discarded because it is not a text message");
        }
    }


    protected void putTaskOnQueue(Task task) {
        if (task == null) {
            return;
        }
        String messageBody = task.getId().toString();
        try {
            TextMessage tm = session.createTextMessage(messageBody);
            logger.info("Sending event {}", messageBody);
            sender.send(tm);
            logger.info("Sent event {}", messageBody);
        } catch (JMSException e) {
            logger.warn("Unable to send JMS message", e);
        }


    }
}
