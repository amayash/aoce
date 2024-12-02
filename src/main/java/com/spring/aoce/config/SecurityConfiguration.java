package com.spring.aoce.config;

import com.spring.aoce.controller.UserSignUpMvcController;
import com.spring.aoce.dto.UserSignUpDto;
import com.spring.aoce.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.time.LocalDate;
import java.util.Objects;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
    private static final String LOGIN_URL = "/login";
    private final UserService userService;
    private final PasswordEncoderConfiguration passwordEncoderConfiguration;

    @PostConstruct
    private void createAdminAfterStartup() {
        final String admin = "admin";
        final String adminEmail = "admin@admin.ru";
        if (Objects.isNull(UserService.findByEmail(adminEmail))) {
            log.info("Admin sender successfully created");
            userService.create(new UserSignUpDto(adminEmail, admin, admin, admin,
                    "+7(111)111-11-11", LocalDate.now(), "111"));
        }
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(UserSignUpMvcController.SIGNUP_URL).permitAll()
                        .requestMatchers(HttpMethod.GET, LOGIN_URL).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage(LOGIN_URL)
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logoutConfigurer -> logoutConfigurer.invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoderConfiguration.createPasswordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/css/**")
                .requestMatchers("/js/**")
                .requestMatchers("/templates/**")
                .requestMatchers("/webjars/**");
    }
}
