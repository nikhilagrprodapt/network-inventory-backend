package com.company.network_inventory.audit.repository;

import com.company.network_inventory.audit.entity.AuditLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findTop50ByOrderByCreatedAtDesc();

    @Query("""
        SELECT a FROM AuditLog a
        WHERE (:actor IS NULL OR :actor = '' OR LOWER(a.actor) LIKE LOWER(CONCAT('%', :actor, '%')))
          AND (:action IS NULL OR :action = '' OR LOWER(a.action) LIKE LOWER(CONCAT('%', :action, '%')))
          AND (:entityType IS NULL OR :entityType = '' OR LOWER(a.entityType) LIKE LOWER(CONCAT('%', :entityType, '%')))
          AND (:since IS NULL OR a.createdAt >= :since)
        ORDER BY a.createdAt DESC
    """)
    List<AuditLog> search(
            @Param("actor") String actor,
            @Param("action") String action,
            @Param("entityType") String entityType,
            @Param("since") LocalDateTime since,
            Pageable pageable
    );
}
