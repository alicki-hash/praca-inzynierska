package com.example.application.data.entity;

import javax.persistence.Entity;

import com.example.application.data.AbstractEntity;
import org.hibernate.annotations.Formula;

@Entity
public class WorkScope extends AbstractEntity {
    private String name;

    public WorkScope() {}

    public WorkScope(String name) {
        this.name = name;
    }

    @Formula("select count(e.id) from expense e where e.work_scope_id = id")
    private int expenseCount;

    public int getExpenseCount() {
        return expenseCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
