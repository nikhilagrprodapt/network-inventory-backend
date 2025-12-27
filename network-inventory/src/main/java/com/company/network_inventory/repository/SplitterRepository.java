package com.company.network_inventory.repository;

import com.company.network_inventory.entity.Splitter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SplitterRepository extends JpaRepository<Splitter, Long> {
    List<Splitter> findByFdh_FdhId(Long fdhId);
}
