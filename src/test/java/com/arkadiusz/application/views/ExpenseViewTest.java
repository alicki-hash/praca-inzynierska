package com.arkadiusz.application.views;

import com.arkadiusz.application.data.entity.Expense;
import com.arkadiusz.application.data.entity.WorkScope;
import org.junit.Before;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseViewTest {
    private Expense expense = new Expense();
    private WorkScope workScope = new WorkScope("Test workscope");

    GridCrud<Expense> grid = new GridCrud<>(Expense.class);

    @Before
    public void setupData() {
        expense.setTitle("Test expense");
        expense.setDateOfExpense(LocalDate.EPOCH);
        expense.setAmount(BigDecimal.TEN);
        expense.setWorkScope(workScope);
        expense.setDescriptionOfExpense("This is workscope test description");

    }



}
