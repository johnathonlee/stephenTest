package com.acme.model;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

/**
 * User: bven
 * Date: 9/8/16.
 */
@RequestScoped
public class AccountCdi {
    @Inject
    @AcmeEntityManager
    EntityManager em;

    public Long createAccount(String name) {
        Account account = new Account(name);
        em.persist(account);
        return account.getId();
    }

    public void updateAccount(Long id, String name) {
        Account account = em.find(Account.class, id);
        if ( account == null){
            throw new EntityNotFoundException();
        }

        account.setName(name);
        em.merge(account);

    }

}
