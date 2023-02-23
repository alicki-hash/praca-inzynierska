package com.arkadiusz.application.data.service;

import com.arkadiusz.application.data.entity.Expense;
import com.arkadiusz.application.data.entity.WorkScope;
import com.arkadiusz.application.data.entity.Status;
import com.arkadiusz.application.data.repository.ExpenseRepository;
import com.arkadiusz.application.data.repository.WorkScopeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ExpenseService {

    public static final Logger LOGGER = Logger.getLogger(Expense.class.getName());

    private final ExpenseRepository expenseRepository;
    private final WorkScopeRepository workScopeRepository;

    public ExpenseService(ExpenseRepository expenseRepository, WorkScopeRepository workScopeRepository) {
        this.expenseRepository = expenseRepository;
        this.workScopeRepository = workScopeRepository;
    }

    public List<Expense> findAllExpenses(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return expenseRepository.findAll();
        } else {
            return expenseRepository.search(stringFilter);
        }
    }

    public void deleteExpense(Expense expense) {
        expenseRepository.delete(expense);
    }

    public Expense saveExpense(Expense expense) {
        if (expense == null) {
            LOGGER.log(Level.SEVERE, "Wydatek jest pusty!");
            return expense;
        }

        return expenseRepository.save(expense);
    }

    public List<WorkScope> findAllWorkScopes() {
        return workScopeRepository.findAll();
    }

    public List<Status> findAllStatuses() {
        List<Expense> allExpenses = expenseRepository.findAll();
        List<Status> statuses = new ArrayList<>();
        for (Expense expense : allExpenses) {
            statuses.add(expense.getStatus());
        }

        List<Status> statusesWithoutDuplicates = new ArrayList<>(new HashSet<>(statuses));
        return statusesWithoutDuplicates;
    }

    public BigDecimal sumAllExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        BigDecimal expensesSum = BigDecimal.ZERO;

        for (Expense expense : expenses) {
            expensesSum = expensesSum.add(expense.getAmount());
        }
        return expensesSum;
    }

    public BigDecimal sumExpensesByWorkscope(String workScopeName) {
        BigDecimal sum = new BigDecimal(BigInteger.ZERO);
        List<Expense> expenses = findAllExpenses("");

        for (Expense expense : expenses) {
            if (expense.getWorkScope().getName() == workScopeName) {
                sum = sum.add(expense.getAmount());
            }
        }
        return sum;
    }


    public BigDecimal sumExpensesByDate(int year, int month) {
        BigDecimal sum = new BigDecimal(BigInteger.ZERO);
        List<Expense> expenses = findAllExpenses("");

        for (Expense expense : expenses) {
            if (expense.getDateOfExpense().getYear() == year &&
                    expense.getDateOfExpense().getMonthValue() == month)
                sum = sum.add(expense.getAmount());
        }
        return sum;
    }

    public List<Integer> getAllExpensesYears() {
        List<Expense> expenses = findAllExpenses("");
        List expensesYear = new ArrayList();
        for (Expense expense : expenses) {
            expensesYear.add(expense.getDateOfExpense().getYear());
        }

        List expensesYearWithoutDuplicates = new ArrayList<>(new HashSet<>(expensesYear));

        return expensesYearWithoutDuplicates;
    }

}
