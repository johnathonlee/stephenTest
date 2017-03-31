package com.acme.tests;


import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * User: bven
 * Date: 4/28/16.
 */
public class JndiFixture implements Closeable {

    private static Logger logger = Logger.getLogger("test");

    private final String hostName;
    private final String httpPort;
    private final String userName;
    private final String password;

    public JndiFixture() {

        hostName = System.getProperty("remote.connection.default.host", "127.0.0.1");
        httpPort = System.getProperty("remote.connection.default.port", "8080");
        userName = System.getProperty("remote.connection.default.username", "acme");
        password = System.getProperty("remote.connection.default.password", "acme");

        Properties clientProp = new Properties();

        clientProp.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        clientProp.put("remote.connections", "default");
        clientProp.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
        clientProp.put("remote.connection.default.port", httpPort);
        clientProp.put("remote.connection.default.host", hostName);
        clientProp.put("remote.connection.default.username", userName);
        clientProp.put("remote.connection.default.password", password);

        EJBClientConfiguration cc = new PropertiesBasedEJBClientConfiguration(clientProp);
        ContextSelector<EJBClientContext> selector = new ConfigBasedEJBClientContextSelector(cc);
        ContextSelector<EJBClientContext> previousContext = EJBClientContext.setSelector(selector);
        if (previousContext.getCurrent() != null) {

            try {
                previousContext.getCurrent().close();
            } catch (IOException e) {
                logger.fine("Exception cleaning up context " + e.getMessage());
            }
        }

    }

    public <T> T lookup(String name) throws NamingException {
        Context context = getJNDIContext();
        try {
            return (T) context.lookup(name);
        } finally {
            close(context);
        }
    }

    public void close(Context context) {
        try {
            context.close();
        } catch (NamingException e) {
            logger.warning(e.getMessage());
        }
    }

    public Context getJNDIContext()
            throws NamingException {
        return getJNDIContextForUser(null, null);
    }

    public Context getJNDIContextForUser(String user, String password)
            throws NamingException {

        // properties are read from the jboss-ejb-client.properties file.
        // currently not doing anything with user/password but we need to look at this.
        final Properties ejbProperties = new Properties();
        ejbProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        ejbProperties.put("jboss.naming.client.ejb.context", true);
        return new InitialContext(ejbProperties);
    }


    @Override
    public void close() throws IOException {
        if (EJBClientContext.getCurrent() != null) {
            EJBClientContext.getCurrent().close();
        }
    }

    public String getHostName() {
        return hostName;
    }

    public String getHttpPort() {
        return httpPort;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
