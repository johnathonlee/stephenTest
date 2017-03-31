package com.acme.model;



import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

import javax.jms.*;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import org.slf4j.Logger;

/**
 * User: bven
 * Date: 9/8/16.
 */
public class EntityListener {

    Logger logger = LoggerFactory.getLogger("test");

    @Resource(mappedName = "java:/RemoteFuseJMS")
    private TopicConnectionFactory topicConnectionFactory;

    @PostPersist
    public void postPersist(Object o) {
        sendEvent(((HasId)o).getId().toString(), o.getClass().getCanonicalName(), "CREATE");
    }

    @PostUpdate
    public void postUpdate(Object o){
        sendEvent(((HasId)o).getId().toString(), o.getClass().getCanonicalName(), "UPDATE");
    }

    private void sendEvent(String body, String type, String action){
            TopicConnection connection = null;
            TopicSession session = null;
            TopicPublisher publisher = null;

            try {

                connection = topicConnectionFactory.createTopicConnection();
                session = connection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
                publisher = session.createPublisher(session.createTopic("lifecycle"));

                logger.info("Sending event {} for type {} and action {}",  body, type, action);

                TextMessage tm = session.createTextMessage(body);
                tm.setStringProperty("type", type);
                tm.setStringProperty("action", action);

                publisher.send(tm);


            } catch (JMSException e) {
                logger.error( "Unable to send JMS message ", e);
            }finally{
                if ( publisher != null){
                    try {
                        publisher.close();
                    } catch (JMSException e) {
                    }
                }
                if ( session != null){
                    try {
                        session.close();
                    } catch (JMSException e) {
                    }
                }
                if (connection != null){
                    try {
                        connection.close();
                    } catch (JMSException e) {
                    }
                }
            }

    }



}
