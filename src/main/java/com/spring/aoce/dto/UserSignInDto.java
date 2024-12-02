package com.spring.aoce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignInDto {
    @NotBlank(message = "Почта обязательна")
    @Size(min = 5, max = 64)
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Некорректное значение почты")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 5, max = 64)
    private String password;
}
