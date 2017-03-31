package com.acme.common;

import javax.enterprise.context.RequestScoped;
import java.io.Serializable;

/**
 * User: bven
 * Date: 9/8/16.
 */
@RequestScoped
public class User implements Serializable{
    private String name;

    public User(){
        name = "default";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
