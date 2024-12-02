package com.spring.aoce.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpDto {
    @NotBlank(message = "Почта обязательна")
    @Size(min = 5, max = 64)
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Некорректное значение почты")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 5, max = 64)
    private String password;

    @NotBlank(message = "Подтверждение пароля обязательно")
    @Size(min = 5, max = 64)
    private String passwordConfirm;

    @NotBlank(message = "ФИО обязательно")
    @Size(min = 8, max = 64)
    private String fullName;

    @NotBlank(message = "Телефон обязателен")
    @Size(min = 16, max = 16)
    @Pattern(regexp = "\\+7 \\d{3} \\d{3}-\\d{2}-\\d{2}", message = "Некорректный формат телефона")
    private String phone;

    @NotNull(message = "Дата рождения обязательна")
    private LocalDate birthday;

    @Nullable
    private String code;
}
