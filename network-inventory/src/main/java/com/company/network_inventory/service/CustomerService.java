package com.company.network_inventory.service;

import com.company.network_inventory.dto.CustomerAssignSplitterRequest;
import com.company.network_inventory.dto.CustomerCreateRequest;
import com.company.network_inventory.dto.CustomerResponse;
import com.company.network_inventory.dto.CustomerUpdateRequest;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerCreateRequest request);
    CustomerResponse getCustomer(Long customerId);
    List<CustomerResponse> getAllCustomers();
    void deleteCustomer(Long customerId);

    CustomerResponse assignSplitter(Long customerId, CustomerAssignSplitterRequest request);

    CustomerResponse updateCustomer(Long customerId, CustomerUpdateRequest request);

}
