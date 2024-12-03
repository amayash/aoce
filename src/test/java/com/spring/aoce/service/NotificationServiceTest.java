package com.spring.aoce.service;

import com.spring.aoce.dto.NotificationDto;
import com.spring.aoce.entity.Notification;
import com.spring.aoce.entity.Request;
import com.spring.aoce.entity.User;
import com.spring.aoce.entity.UserNotification;
import com.spring.aoce.entity.enums.UserRole;
import com.spring.aoce.repository.NotificationRepository;
import com.spring.aoce.repository.UserNotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.spring.aoce.data.NotificationData.createNotification;
import static com.spring.aoce.data.RequestData.createRequest;
import static com.spring.aoce.data.UserData.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserNotificationRepository userNotificationRepository;

    private final MockedStatic<UserService> mockedStaticUserService = mockStatic(UserService.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationService.setNotificationRepository(notificationRepository);
    }

    @AfterEach
    void tearDown() {
        mockedStaticUserService.close();
    }

    @Test
    void testGetAllForAdmin() {
        User admin = createUser();
        admin.setRole(UserRole.ADMIN);
        Notification notification = createNotification();
        notification.setSender(createUser());

        Page<Notification> notifications = new PageImpl<>(List.of(notification));
        mockedStaticUserService.when(UserService::findByPrincipal).thenReturn(admin);
        when(notificationRepository.findAll(any(PageRequest.class))).thenReturn(notifications);

        Page<NotificationDto> result = notificationService.getAll(0, 10);

        assertEquals(1, result.getTotalElements());
        verify(notificationRepository).findAll(any(PageRequest.class));
    }

    @Test
    void testGetAllForRegularUser() {
        User user = createUser();
        Notification notification = createNotification();
        notification.setSender(createUser());

        Page<Notification> notifications = new PageImpl<>(List.of(notification));
        mockedStaticUserService.when(UserService::findByPrincipal).thenReturn(user);
        when(notificationRepository.findBySenderId(anyLong(), any(PageRequest.class))).thenReturn(notifications);

        Page<NotificationDto> result = notificationService.getAll(0, 10);

        assertEquals(1, result.getTotalElements());
        verify(notificationRepository).findBySenderId(anyLong(), any(PageRequest.class));
    }

    @Test
    void testCreateNotification() {
        Request request = createRequest();
        Notification notification = createNotification();
        when(UserService.findByPrincipal()).thenReturn(createUser());
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.create(request);

        assertNotNull(result);
        assertEquals(notification.getId(), result.getId());
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void testMarkAsRead() {
        Notification notification = createNotification();
        User user = createUser();
        when(UserService.findByPrincipal()).thenReturn(user);
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));

        notificationService.markAsRead(notification.getId());

        verify(userNotificationRepository).save(any(UserNotification.class));
        verify(notificationRepository).save(notification);
    }

    @Test
    void testGetEntityFound() {
        Notification notification = createNotification();
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));

        Notification result = NotificationService.getEntity(notification.getId());

        assertNotNull(result);
        assertEquals(notification.getId(), result.getId());
    }

    @Test
    void testGetEntityNotFound() {
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> NotificationService.getEntity(1L));
    }

    @Test
    void testFindBySenderIdAndRequestId() {
        UserNotification userNotification = new UserNotification();
        when(userNotificationRepository.findByUserIdAndNotificationRequestId(anyLong(), anyLong()))
                .thenReturn(userNotification);

        UserNotification result = notificationService.findBySenderIdAndRequestId(1L, 1L);

        assertNotNull(result);
        verify(userNotificationRepository).findByUserIdAndNotificationRequestId(anyLong(), anyLong());
    }
}
