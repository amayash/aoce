package com.spring.aoce.repository;


import com.spring.aoce.entity.RequestPrinter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestPrinterRepository extends JpaRepository<RequestPrinter, Long> {
    Page<RequestPrinter> findAllByUserId(Long userId, Pageable pageable);
}
