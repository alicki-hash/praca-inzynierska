package com.arkadiusz.application.data.entity;

import com.arkadiusz.application.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Expense extends AbstractEntity {

    @NotEmpty(message = "Tytuł nie może być pusty!")
    private String title = "";

    private LocalDate dateOfExpense;


    private BigDecimal amount;

    private Status status;

    @ManyToOne
    private WorkScope workScope;

    private String descriptionOfExpense = "";

    public WorkScope getWorkScope() {
        return workScope;
    }

    public void setWorkScope(WorkScope workScope) {
        this.workScope = workScope;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDateOfExpense() {
        return dateOfExpense;
    }

    public void setDateOfExpense(LocalDate dateOfExpense) {
        this.dateOfExpense = dateOfExpense;
    }

    public String getDescriptionOfExpense() {
        return descriptionOfExpense;
    }

    public void setDescriptionOfExpense(String descriptionOfExpense) {
        this.descriptionOfExpense = descriptionOfExpense;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // for test data
    public void setAmount(Double amount) {
        this.amount = BigDecimal.valueOf(amount);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
