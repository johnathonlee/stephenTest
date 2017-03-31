package com.acme.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * User: bven
 * Date: 9/8/16.
 */
@RequestScoped  // <---- this will cause a failure in the tests
public class EntityManagerProducer {

    private static Logger logger = LoggerFactory.getLogger(EntityManagerProducer.class);

    @PersistenceUnit(name = "acme")
    EntityManagerFactory emf;

    @Produces
    @RequestScoped  // causes test failure
    @AcmeEntityManager
    public EntityManager getEntityManager() {
        EntityManager em = emf.createEntityManager();
        logger.trace("created an entity manager");
        return em;
    }

    public void closeEntityManager(@Disposes @AcmeEntityManager EntityManager em) {
        try {
            em.close();
            logger.trace("Closed the entity manager");
        } catch (RuntimeException e) {
            logger.warn("Exception closing the entity manager {}", e.getMessage());
        }
    }
}
