package com.mysite.expense.repository;

import com.mysite.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<Expense> findByExpenseId(String expenseId);

//    List<Expense> findByNameContaining(String keyword);

    List<Expense> findByNameContainingAndDateBetweenAndUserId(String keyword, Date start, Date end, Long id);

    List<Expense> findByUserId(Long Id);

    List<Expense> findByDateBetweenAndUserId(Date start, Date end, Long id);
}
