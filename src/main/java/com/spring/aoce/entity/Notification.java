package com.spring.aoce.entity;

import com.spring.aoce.dto.NotificationDto;
import com.spring.aoce.util.DateUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.spring.aoce.service.RequestService.getEntity;
import static com.spring.aoce.service.UserService.findByEmail;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false, updatable = false)
    private User sender;

    @OneToMany(mappedBy = "notification")
    private List<UserNotification> users = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false, updatable = false)
    private Request request;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    public Notification(NotificationDto notificationDto) {
        this.sender = findByEmail(notificationDto.getUser());
        this.request = getEntity(notificationDto.getRequest().getId());
        this.sentAt = DateUtils.parseToLocalDateTime(notificationDto.getSentAt());
    }
}
