package com.acme.tests;

import org.junit.After;
import org.junit.Before;

import javax.naming.NamingException;

import static org.junit.Assert.fail;

/**
 * User: dev
 * Date: 11/25/16.
 */
public abstract class AbstractIntegrationTest {
    private JndiFixture jndi;

    @Before
    public void init() throws Exception {
        jndi = new JndiFixture();
    }

    @After
    public void tearDown() throws Exception{
        jndi.close();
    }

    <T> T getBean(String beanName) {
        try {
            return jndi.lookup(beanName);
        } catch (NamingException e) {
            fail("Unable to get bean: " + e.getMessage());
        }
        return null;
    }
}
