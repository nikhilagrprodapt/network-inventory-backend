package com.company.network_inventory.controller;

import com.company.network_inventory.dto.CustomerCreateRequest;
import com.company.network_inventory.dto.CustomerResponse;
import com.company.network_inventory.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.company.network_inventory.dto.CustomerAssignSplitterRequest;


import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public CustomerResponse create(@Valid @RequestBody CustomerCreateRequest request) {
        return customerService.createCustomer(request);
    }

    @PutMapping("/{id}/assign-splitter")
    public CustomerResponse assignSplitter(
            @PathVariable Long id,
            @Valid @RequestBody CustomerAssignSplitterRequest request
    ) {
        return customerService.assignSplitter(id, request);
    }

    @GetMapping("/{id}")
    public CustomerResponse getOne(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @GetMapping
    public List<CustomerResponse> getAll() {
        return customerService.getAllCustomers();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "Customer deleted: " + id;
    }
}
