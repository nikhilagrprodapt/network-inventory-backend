package com.company.network_inventory.repository;

import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.enums.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    long countByStatus(CustomerStatus status);

    List<Customer> findTop6ByOrderByCustomerIdDesc();

    List<Customer> findBySplitter_SplitterId(Long splitterId);

    boolean existsBySplitter_SplitterIdAndSplitterPortAndCustomerIdNot(
            Long splitterId,
            Integer splitterPort,
            Long customerId
    );

    boolean existsBySplitter_SplitterIdAndSplitterPort(Long splitterId, Integer splitterPort);
}
