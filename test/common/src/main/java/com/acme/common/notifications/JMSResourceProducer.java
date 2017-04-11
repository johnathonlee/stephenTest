package com.acme.common.notifications;

import com.acme.common.annotations.TaskQueue;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.jms.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Dependent
public class JMSResourceProducer {
    static final Logger logger = LoggerFactory.getLogger(JMSResourceProducer.class);

    @Resource(mappedName = "java:/RemoteFuseJMS" )
    private QueueConnectionFactory queueConnectionFactory;

    @Produces
    @TaskQueue
    public QueueConnection createTaskConnection() throws JMSException {
        logger.info("Creating queue connection ...");
        QueueConnection connection = queueConnectionFactory.createQueueConnection();
        logger.info("Created queue connection " + connection);
        return connection;
    }

    public void closeQueueConnection(@Disposes @TaskQueue QueueConnection qc) throws JMSException {
        qc.close();
    }

    @Produces
    @TaskQueue
    public QueueSession createQueueSession(@TaskQueue QueueConnection qc) throws JMSException {
        logger.info("Creating queue session using " + qc + " ...");
        QueueSession session = qc.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
        logger.info("Created queue session " + session);
        return session;
    }

    public void closeQueueSession(@Disposes @TaskQueue QueueSession qs) throws JMSException {
        qs.close();
    }

    @Produces
    @TaskQueue
    public QueueSender createQueueSender(@TaskQueue QueueSession qs) throws JMSException {
        logger.info("Creating queue sender using " + qs + " ...");
        QueueSender sender = qs.createSender(qs.createQueue("TASKQueue"));
        logger.info("Created queue sender " + sender);
        return sender;
    }

    public void closeQueueSender(@Disposes @TaskQueue QueueSender qs) throws JMSException {
        qs.close();
    }
}
