package com.mysite.expense.config;

import com.mysite.expense.service.CustomUserDetailsService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration  // Bean 등록할때 쓰는 어노테이션
@EnableWebSecurity
public class CustomSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authz) ->
                        authz
                                .requestMatchers("/js/**", "/css/**").permitAll()
                                .requestMatchers("/", "/login", "/register").permitAll()
                                .anyRequest().authenticated()
                )   // RequestMatcher 의 주소들은 허가하고 그 외의 주소는 인증을 요구함
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/login")
                                .failureUrl("/login")
                                .defaultSuccessUrl("/expenses")
                                .usernameParameter("email")
                                .passwordParameter("password")
                        // 인증시 로그인 페이지를 지정하고 실패시 주소 지정, 성공주소 지정 (username => email, password)
                );

        return http.build();
    }

    // 정적파일 : 자주쓰는 js, css 파일등의 정적 파일은 예외로 처리
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->
                web.ignoring().requestMatchers("/css/**", "/js/**", "/error");
    }

    // 암호화 객체
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @SuppressWarnings("unused")
//    private final CustomUserDetailsService customUserDetailsService;
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

}
