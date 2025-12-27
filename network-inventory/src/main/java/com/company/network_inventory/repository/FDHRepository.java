package com.company.network_inventory.repository;

import com.company.network_inventory.entity.FDH;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FDHRepository extends JpaRepository<FDH, Long> {
    List<FDH> findByHeadend_HeadendId(Long headendId);
}
