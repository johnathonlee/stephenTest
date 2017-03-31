package com.acme.common;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

/**
 * User: bven
 * Date: 9/9/16.
 */
public class JNDILookupUtil {
    public static <T> T getCDIManagedBean(Class<T> beanClass) {
        T result;
        BeanManager beanManager;
        Bean<T> bean;
        CreationalContext<T> creationalContext;

        beanManager = CDI.current().getBeanManager();
        bean = (Bean<T>) beanManager.getBeans(beanClass).stream().findFirst().get();
        creationalContext = beanManager.createCreationalContext(bean);
        result = (T) beanManager.getReference(bean, beanClass, creationalContext);

        return result;
    }
}
