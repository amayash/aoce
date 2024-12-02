package com.spring.aoce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EquipmentImage {
    @Id
    @Column(updatable = false, unique = true, nullable = false)
    private UUID s3_key;

    @ManyToOne
    @JoinColumn(name = "equipment_id", unique = true, updatable = false, nullable = false)
    private Equipment equipment;
}
