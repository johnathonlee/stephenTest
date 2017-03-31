package com.acme.common.notifications;

import com.acme.common.annotations.TaskQueue;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.jms.*;


@Dependent
public class JMSResourceProducer {

    @Resource(mappedName = "java:/RemoteFuseJMS" )
    private QueueConnectionFactory queueConnectionFactory;

    @Produces
    @TaskQueue
    public QueueConnection createTaskConnection() throws JMSException {
        return queueConnectionFactory.createQueueConnection();
    }

    public void closeQueueConnection(@Disposes @TaskQueue QueueConnection qc) throws JMSException {
        qc.close();
    }

    @Produces
    @TaskQueue
    public QueueSession createQueueSession(@TaskQueue QueueConnection qc) throws JMSException {
        return qc.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
    }

    public void closeQueueSession(@Disposes @TaskQueue QueueSession qs) throws JMSException {
        qs.close();
    }

    @Produces
    @TaskQueue
    public QueueSender createQueueSender(@TaskQueue QueueSession qs) throws JMSException {
        return qs.createSender(qs.createQueue("TASKQueue"));
    }

    public void closeQueueSender(@Disposes @TaskQueue QueueSender qs) throws JMSException {
        qs.close();
    }
}
