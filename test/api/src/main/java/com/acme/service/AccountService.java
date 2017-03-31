package com.acme.service;

/**
 * User: bven
 * Date: 9/8/16.
 */
public interface AccountService {

    Long createAccount(String name);
    void updateAccount(Long id, String name);
}
