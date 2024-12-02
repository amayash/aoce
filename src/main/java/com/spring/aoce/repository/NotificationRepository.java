package com.spring.aoce.repository;

import com.spring.aoce.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findBySenderId(Long id, Pageable pageable);

    Collection<Notification> findBySenderId(Long id);
}
