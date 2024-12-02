package com.spring.aoce.repository;

import com.spring.aoce.entity.RequestComputer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestComputerRepository extends JpaRepository<RequestComputer, Long> {
    Page<RequestComputer> findAllByUserId(Long userId, Pageable pageable);
}
