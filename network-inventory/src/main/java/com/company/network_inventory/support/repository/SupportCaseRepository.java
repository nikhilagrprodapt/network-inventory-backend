package com.company.network_inventory.support.repository;

import com.company.network_inventory.support.entity.SupportCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportCaseRepository extends JpaRepository<SupportCase, Long> {
    List<SupportCase> findByCustomer_CustomerIdOrderByCreatedAtDesc(Long customerId);
}
