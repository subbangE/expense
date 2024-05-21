package com.mysite.expense.controller;

import com.mysite.expense.dto.UserDTO;
import com.mysite.expense.entity.User;
import com.mysite.expense.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService uService;

    @GetMapping({"/login", "/"})
    public String showLoginPage(Principal principal) {
        if(principal == null) {     // principal 객체는 시큐리티 인증시 유저의 정보를 담아 유지하는 객체
            return "login";         // principal 객체가 null이란 것은 인증이 안된 상태를 의미
        }
        return "redirect:/expenses";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register") // BindingResult는 @ModelAttribute(모델 속성), @RequestBody, @Requestpart 뒤에 위치해야함
    public String register(@Valid @ModelAttribute("user") UserDTO user, BindingResult result, Model model) {
        System.out.println("User DTO 객체 : " + user);
        if(result.hasErrors()) {
            return "register";
        }
        uService.save(user);
        model.addAttribute("successMsg", true);
        return "response";
    }
}
