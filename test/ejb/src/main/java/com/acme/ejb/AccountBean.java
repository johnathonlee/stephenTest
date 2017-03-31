package com.acme.ejb;

import com.acme.model.AccountCdi;
import com.acme.service.AccountService;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Logger;

/**
 * User: bven
 * Date: 9/8/16.
 */
@Stateless
@Remote(AccountService.class)
public class AccountBean implements AccountService {

    @Inject
    AccountCdi accountCdi;

    @Override
    public Long createAccount(String name) {

        return accountCdi.createAccount(name);
    }

    @Override
    public void updateAccount(Long id, String name) {
        accountCdi.updateAccount(id, name);
    }
}
