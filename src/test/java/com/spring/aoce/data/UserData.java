package com.spring.aoce.data;

import com.spring.aoce.entity.User;
import com.spring.aoce.entity.enums.UserRole;

import java.time.LocalDate;

public class UserData {
    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setBirthday(LocalDate.now());
        user.setEmail("test@email.ru");
        user.setPassword("password");
        user.setRole(UserRole.USER);
        user.setFullName("Full name");
        user.setPhone("+7(111)111-11-11");
        return user;
    }
}
