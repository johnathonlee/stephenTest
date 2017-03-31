package com.acme.ejb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;

import com.acme.model.AcmeEntityManager;
import com.acme.model.Task;
import org.jboss.ejb3.annotation.ResourceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: dev
 * Date: 11/25/16.
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/queue/TASKQueue"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "50")
})
@ResourceAdapter(value = "activemq-ra")
public class TaskMDB implements MessageListener {

    static final Logger logger = LoggerFactory.getLogger(TaskMDB.class);

    @Inject
    @AcmeEntityManager
    EntityManager em;


    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                TextMessage text = (TextMessage) message;
                Long id = Long.parseLong(text.getText());
                logger.trace("Looking for task with id {}", id);

                Task t = em.find(Task.class, id);

                for (int retry = 1 ; t == null && retry <= 5 ; retry++) {
                    logger.warn("No task found with id {}, sleep and retry ...", id);
                    Thread.sleep(2000);
                    t = em.find(Task.class, id);
                }

                if (t == null) {
                    logger.error("No task found with id {}", id);
                } else {
                    logger.info("Task found with id {}", id);
                    logger.trace("Updating task {}", id);
                    t.setIndex(t.getIndex() + 1);
                    em.merge(t);
                }
            } catch (Exception e) {
                logger.warn("Unable to process message", e);
            }

        } else {
            logger.info("Received message is discarded because it is not a text message");
        }
    }
}
