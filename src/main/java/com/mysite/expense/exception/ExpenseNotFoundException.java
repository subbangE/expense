package com.mysite.expense.exception;

public class ExpenseNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String message;

    public ExpenseNotFoundException(String message) {
        this.message = message;
    }
}
