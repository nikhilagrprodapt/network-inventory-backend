package com.company.network_inventory.audit.repository;

import com.company.network_inventory.audit.entity.AuditLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // ✅ Your controller is calling this
    List<AuditLog> findTop50ByOrderByCreatedAtDesc();

    // ✅ Optional (nice for paging later)
    List<AuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
