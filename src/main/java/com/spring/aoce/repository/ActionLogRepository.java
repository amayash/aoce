package com.spring.aoce.repository;

import com.spring.aoce.entity.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
    ActionLog findByRequestId(Long id);
}
