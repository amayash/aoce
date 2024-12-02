package com.spring.aoce.repository;

import com.spring.aoce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByEmailIgnoreCase(String username);

    User findUserByPhone(String phone);
}
