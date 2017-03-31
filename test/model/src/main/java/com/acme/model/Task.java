package com.acme.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * User: dev
 * Date: 11/25/16.
 */
@Entity
@Table(name = "Task" )
public class Task implements Serializable, HasId{

    @Id
    @SequenceGenerator(allocationSize = 1, sequenceName = "ACME_SEQ", name = "ACME_SEQ_NAME")
    @GeneratedValue(generator = "ACME_SEQ_NAME")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "IDX")
    private Integer index = 0;

    public Task(){}

    public Long getId() {
        return id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
