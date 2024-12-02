package com.spring.aoce.controller;

import com.spring.aoce.dto.UserSignUpDto;
import com.spring.aoce.entity.User;
import com.spring.aoce.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping(UserSignUpMvcController.SIGNUP_URL)
public class UserSignUpMvcController {
    public static final String SIGNUP_URL = "/signup";

    private final UserService userService;

    @GetMapping
    public String showSignupForm(Model model) {
        model.addAttribute("userDto", new UserSignUpDto());
        return "signup";
    }

    @PostMapping
    public String signup(Model model, @ModelAttribute("userDto") @Valid UserSignUpDto userSignupDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors().stream()
                    .collect(Collectors.groupingBy(
                            FieldError::getField,
                            Collectors.mapping(
                                    error -> Objects.isNull(error.getDefaultMessage()) ?
                                            "Неизвестная ошибка валидации" : error.getDefaultMessage(),
                                    Collectors.joining(", ")
                            )
                    )));
            return "signup";
        }

        try {
            final User user = userService.create(userSignupDto);
            return "redirect:/login?created=" + user.getEmail();
        } catch (ValidationException e) {
            model.addAttribute("errors", e.getMessage());
            return "signup";
        }
    }
}
