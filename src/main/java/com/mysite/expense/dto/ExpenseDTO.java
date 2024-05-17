package com.mysite.expense.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDTO {
    private Long id;
    private String expenseId;
    private String name;
    private String description;
    private long amount;
    private Date date;
    private String dateString;  //날짜를 표시하기위한 문자열
}
