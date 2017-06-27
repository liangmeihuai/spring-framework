package org.springframework.meihuai.model;

import org.springframework.stereotype.Component;

/**
 * Created by tend on 2017/6/21.
 */
@Component
public class Person {
    private Integer id=123;
    private String name="aaa";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
