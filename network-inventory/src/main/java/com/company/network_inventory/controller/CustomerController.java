package com.company.network_inventory.controller;

import com.company.network_inventory.dto.CustomerAssignSplitterRequest;
import com.company.network_inventory.dto.CustomerCreateRequest;
import com.company.network_inventory.dto.CustomerDeactivateRequest;
import com.company.network_inventory.dto.CustomerUpdateRequest;
import com.company.network_inventory.service.CustomerService;
import com.company.network_inventory.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ApiResponse create(@Valid @RequestBody CustomerCreateRequest request) {
        return ApiResponse.ok("Customer created", customerService.createCustomer(request));
    }

    @GetMapping("/{id}")
    public ApiResponse get(@PathVariable Long id) {
        return ApiResponse.ok("Customer fetched", customerService.getCustomer(id));
    }

    @GetMapping
    public ApiResponse all() {
        return ApiResponse.ok("Customers fetched", customerService.getAllCustomers());
    }

    @PutMapping("/{id}")
    public ApiResponse update(@PathVariable Long id, @Valid @RequestBody CustomerUpdateRequest request) {
        return ApiResponse.ok("Customer updated", customerService.updateCustomer(id, request));
    }

    @PutMapping("/{id}/assign-splitter")
    public ApiResponse assignSplitter(@PathVariable Long id, @Valid @RequestBody CustomerAssignSplitterRequest request) {
        return ApiResponse.ok("Splitter assigned", customerService.assignSplitter(id, request));
    }

    @PostMapping("/{id}/deactivate")
    public ApiResponse deactivate(@PathVariable Long id, @Valid @RequestBody CustomerDeactivateRequest request) {
        return ApiResponse.ok("Customer deactivated", customerService.deactivateCustomer(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ApiResponse.ok("Customer deleted", null);
    }
}
