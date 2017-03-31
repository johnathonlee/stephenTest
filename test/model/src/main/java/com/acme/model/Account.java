package com.acme.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: bven
 * Date: 9/8/16.
 */
@Entity
@Table(name = "Account" )
@EntityListeners({EntityListener.class})
public class Account implements Serializable, HasId{
    @Id
    @SequenceGenerator(allocationSize = 1, sequenceName = "ACME_SEQ", name = "ACME_SEQ_NAME")
    @GeneratedValue(generator = "ACME_SEQ_NAME")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", length = 25)
    private String name;

    protected Account(){}

    public Account(String name){
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
