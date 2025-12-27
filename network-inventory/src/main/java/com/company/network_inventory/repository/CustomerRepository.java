package com.company.network_inventory.repository;

import com.company.network_inventory.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsBySplitter_SplitterIdAndSplitterPort(Long splitterId, Integer splitterPort);

    boolean existsBySplitter_SplitterIdAndSplitterPortAndCustomerIdNot(Long splitterId, Integer splitterPort, Long customerId);

    List<Customer> findBySplitter_SplitterId(Long splitterId);
}
