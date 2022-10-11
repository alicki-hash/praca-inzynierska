package com.example.application.data.entity;

import com.vaadin.flow.component.textfield.NumberField;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;


    @NotNull
    private String investmentName;
    @NotNull
    private LocalDate investmentStartDate;

    @NotNull
    private BigDecimal investmentBudget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Investment() {
    }

    public Investment(String investmentName, LocalDate investmentStartDate, BigDecimal investmentBudget) {
        this.investmentName = investmentName;
        this.investmentStartDate = investmentStartDate;
        this.investmentBudget = investmentBudget;
    }

    public String getInvestmentName() {
        return investmentName;
    }

    public void setInvestmentName(String investmentName) {
        this.investmentName = investmentName;
    }

    public LocalDate getInvestmentStartDate() {
        return investmentStartDate;
    }

    public void setInvestmentStartDate(LocalDate investmentStartDate) {
        this.investmentStartDate = investmentStartDate;
    }

    public BigDecimal getInvestmentBudget() {
        return investmentBudget;
    }


    public void setInvestmentBudget(BigDecimal investmentBudget) {
        this.investmentBudget = investmentBudget;
    }

    // for binder
    public BigDecimal getInvestmentBudget(Investment investment) {
        return investment.getInvestmentBudget();
    }
    public void setInvestmentBudget(Investment investment, BigDecimal bigDecimal) {
        investment.setInvestmentBudget(bigDecimal);
    }
}
