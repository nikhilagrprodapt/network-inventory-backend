package com.company.network_inventory.repository;

import com.company.network_inventory.entity.FiberDropLine;
import com.company.network_inventory.entity.enums.FiberLineStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FiberDropLineRepository extends JpaRepository<FiberDropLine, Long> {

    long countByStatus(FiberLineStatus status);

    List<FiberDropLine> findByFromSplitter_SplitterId(Long splitterId);

    Optional<FiberDropLine> findByToCustomer_CustomerId(Long customerId);
}
