package com.arkadiusz.application.data.repository;

import com.arkadiusz.application.data.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseRepository extends JpaRepository <Expense, Long> {


//    @Query("select c from Contact c " +
//            "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
//            "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
//    List<Contact> search(@Param("searchTerm") String searchTerm);


    @Query("select e from Expense e " +
            "where lower(e.title) like lower(concat('%', :searchTerm, '%')) ")
    List<Expense> search(@Param("searchTerm") String searchTerm);
}
