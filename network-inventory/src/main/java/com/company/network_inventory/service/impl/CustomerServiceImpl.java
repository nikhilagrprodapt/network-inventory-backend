package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.CustomerCreateRequest;
import com.company.network_inventory.dto.CustomerResponse;
import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.Splitter;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.CustomerRepository;
import com.company.network_inventory.repository.SplitterRepository;
import com.company.network_inventory.service.CustomerService;
import com.company.network_inventory.dto.CustomerAssignSplitterRequest;
import com.company.network_inventory.entity.enums.CustomerStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    @Override
    public CustomerResponse assignSplitter(Long customerId, CustomerAssignSplitterRequest request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));

        Splitter splitter = splitterRepository.findById(request.getSplitterId())
                .orElseThrow(() -> new ResourceNotFoundException("Splitter not found: " + request.getSplitterId()));

        int port = request.getSplitterPort();

        // Validate port range
        if (port < 1 || port > splitter.getPortCapacity()) {
            throw new IllegalArgumentException("Invalid port. Splitter supports ports 1 to " + splitter.getPortCapacity());
        }

        // Validate port availability (no duplicate use)
        boolean portTaken = customerRepository
                .existsBySplitter_SplitterIdAndSplitterPortAndCustomerIdNot(splitter.getSplitterId(), port, customerId);
        if (portTaken) {
            throw new IllegalArgumentException("Port " + port + " is already assigned on this splitter.");
        }

        // Assign
        customer.setSplitter(splitter);
        customer.setSplitterPort(port);

        // If customer was pending, you can optionally mark ACTIVE once assigned
        if (customer.getStatus() == CustomerStatus.PENDING) {
            customer.setStatus(CustomerStatus.ACTIVE);
        }

        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }


    private final CustomerRepository customerRepository;
    private final SplitterRepository splitterRepository;

    @Override
    public CustomerResponse createCustomer(CustomerCreateRequest request) {

        Splitter splitter = null;
        if (request.getSplitterId() != null) {
            splitter = splitterRepository.findById(request.getSplitterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Splitter not found: " + request.getSplitterId()));
        }

        Customer customer = Customer.builder()
                .name(request.getName())
                .address(request.getAddress())
                .neighborhood(request.getNeighborhood())
                .plan(request.getPlan())
                .status(request.getStatus())
                .splitter(splitter)
                .splitterPort(request.getSplitterPort())
                .createdAt(LocalDateTime.now())
                .build();

        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    @Override
    public CustomerResponse getCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));
        return toResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public void deleteCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }

    private CustomerResponse toResponse(Customer c) {
        return CustomerResponse.builder()
                .customerId(c.getCustomerId())
                .name(c.getName())
                .address(c.getAddress())
                .neighborhood(c.getNeighborhood())
                .plan(c.getPlan())
                .status(c.getStatus())
                .splitterId(c.getSplitter() != null ? c.getSplitter().getSplitterId() : null)
                .splitterPort(c.getSplitterPort())
                .createdAt(c.getCreatedAt())
                .build();
    }
}