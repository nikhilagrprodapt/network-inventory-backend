package com.company.network_inventory.repository;

import com.company.network_inventory.entity.FiberDropLine;
import com.company.network_inventory.entity.enums.FiberLineStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FiberDropLineRepository extends JpaRepository<FiberDropLine, Long> {
    long countByStatus(FiberLineStatus status);
}
