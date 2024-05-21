package com.mysite.expense.controller;

import com.mysite.expense.dto.ExpenseDTO;
import com.mysite.expense.dto.ExpenseFilterDTO;
import com.mysite.expense.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Filter;

@Controller
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expService;

    @GetMapping("/expenses")
    public String showExpenseList(Model model) {
        List<ExpenseDTO> list = expService.getAllExpenses();
        model.addAttribute("expenses", list);
        model.addAttribute("filter", new ExpenseFilterDTO());
        Long total = expService.totalExpenses(expService.getAllExpenses());
        model.addAttribute("total", total);
        return "expenses-list";
    }

    // 새 비용 컨트롤러 (localhost:8080/createExpense)
    @GetMapping("/createExpense")
    public String createExpense(Model model) {
        model.addAttribute("expense", new ExpenseDTO());
        return "expense-form";
    }

    @PostMapping("/saveOrUpdateExpense")    // @Valid로 사용자가 작성해서 전송한 DTO 객체 검사 (객체에 적용된 어노테이션 위반시 result 에러 발생)
    public String saveOrUpdateExpense(@Valid @ModelAttribute("expense") ExpenseDTO expenseDTO, BindingResult result) throws ParseException {
        System.out.println("입력한 expenseDTO 객체 : " + expenseDTO);
        if(result.hasErrors()) {
            return "expense-form";
        }
        expService.saveExpenseDetails(expenseDTO);
        return "redirect:/expenses";
    }

    @GetMapping("/deleteExpense")
    public String deleteExpense(@RequestParam String id) {
        System.out.println("삭제 비용 번호: " + id);
        expService.deleteExpense(id);
        return "redirect:/expenses";
    }

    @GetMapping("/updateExpense")
    public String updateExpense(@RequestParam String id, Model model) {
        System.out.println("업데이트 아이템: " + id);
        model.addAttribute("expense", expService.getExpenseById(id));
        return "expense-form";
    }

}
