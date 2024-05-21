package com.mysite.expense.repository;

import com.mysite.expense.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // DB에 해당 email이 있는지 찾고 해당 email, password를 확인
    Optional<User> findByEmail(String email);
}
