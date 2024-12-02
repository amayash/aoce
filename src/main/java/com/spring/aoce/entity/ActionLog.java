package com.spring.aoce.entity;

import com.spring.aoce.entity.enums.RequestType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", updatable = false, nullable = false)
    private RequestType actionType;

    @Column(updatable = false, nullable = false)
    private LocalDateTime timestamp;

    @Column(updatable = false, length = 1000)
    private String oldModel;

    @Column(updatable = false, nullable = false, length = 1000)
    private String newModel;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "equipment_id", updatable = false, nullable = false)
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "request_id", updatable = false)
    private Request request;
}

