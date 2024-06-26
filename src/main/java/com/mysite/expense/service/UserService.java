package com.mysite.expense.service;

import com.mysite.expense.dto.UserDTO;
import com.mysite.expense.entity.User;
import com.mysite.expense.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor    // 새로운 필드를 추가할 때 다시 생성자를 만들어줌
public class UserService {
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public void save(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = mapToEntity(userDTO);
        user.setUserId(UUID.randomUUID().toString());
        userRepo.save(user);
    }

    private User mapToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = auth.getName();
        return userRepo.findByEmail(loginUserEmail).orElseThrow(() ->
                new UsernameNotFoundException("이메일을 찾을 수 없습니다."));
    }

}
