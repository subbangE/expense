package com.mysite.expense.controller;

import com.mysite.expense.dto.ExpenseDTO;
import com.mysite.expense.dto.ExpenseFilterDTO;
import com.mysite.expense.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.text.ParseException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExpenseFilterController {

    private final ExpenseService expService;

    @GetMapping("filterExpenses")       // DTO 객체를 생성하고 "filter"라는 이름으로 모델에 추가
    public String filterExpenses(@ModelAttribute("filter") ExpenseFilterDTO expenseFilterDTO, Model model) throws ParseException {
        System.out.println(expenseFilterDTO);
        List<ExpenseDTO> list = expService.getFilterExpenses(expenseFilterDTO);
        model.addAttribute("expenses", list);
        model.addAttribute("filter", expenseFilterDTO);
        Long total = expService.totalExpenses(expService.getFilterExpenses(expenseFilterDTO));
        model.addAttribute("total", total);
        return "expenses-list";
    }
}
