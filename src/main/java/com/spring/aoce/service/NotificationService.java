package com.spring.aoce.service;

import com.spring.aoce.dto.NotificationDto;
import com.spring.aoce.entity.Notification;
import com.spring.aoce.entity.Request;
import com.spring.aoce.entity.User;
import com.spring.aoce.entity.UserNotification;
import com.spring.aoce.entity.UserNotificationKey;
import com.spring.aoce.entity.enums.UserRole;
import com.spring.aoce.repository.NotificationRepository;
import com.spring.aoce.repository.UserNotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;

import static com.spring.aoce.service.UserService.findByPrincipal;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private static NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;

    @Autowired
    public void setNotificationRepository(NotificationRepository notificationRepository) {
        NotificationService.notificationRepository = notificationRepository;
    }

    @Transactional(readOnly = true)
    public Page<NotificationDto> getAll(int page, int size) {
        User currentUser = findByPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("sentAt")));
        if (currentUser.getRole().equals(UserRole.ADMIN)) {
            return notificationRepository.findAll(pageable).map(NotificationDto::new);
        } else {
            return notificationRepository.findBySenderId(currentUser.getId(), pageable).map(NotificationDto::new);
        }
    }

    @Transactional(readOnly = true)
    public Collection<NotificationDto> getAll() {
        User currentUser = findByPrincipal();
        if (currentUser.getRole().equals(UserRole.ADMIN)) {
            return notificationRepository.findAll().stream().map(NotificationDto::new).toList();
        } else {
            return notificationRepository.findBySenderId(currentUser.getId()).stream().map(NotificationDto::new).toList();
        }
    }

    @Transactional
    public UserNotification findBySenderIdAndRequestId(Long userId, Long requestId) {
        return userNotificationRepository.findByUserIdAndNotificationRequestId(userId, requestId);
    }

    @Transactional
    public Notification create(Request request) {
        Notification notification = new Notification();
        notification.setSender(findByPrincipal());
        notification.setRequest(request);
        notification.setSentAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = getEntity(notificationId);
        User currentUser = findByPrincipal();
        try {
            userNotificationRepository.save(new UserNotification(
                    new UserNotificationKey(currentUser.getId(), notification.getId()),
                    currentUser,
                    notification,
                    true
            ));
        } catch (Exception e) {
            log.error("Notification already read");
        }
        notificationRepository.save(notification);
    }

    public static Notification getEntity(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification with id " + id + " not found"));
    }
}
