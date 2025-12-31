package com.company.network_inventory.service.impl;

import com.company.network_inventory.audit.service.AuditService;
import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.FiberDropLine;
import com.company.network_inventory.entity.Splitter;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.CustomerRepository;
import com.company.network_inventory.repository.FiberDropLineRepository;
import com.company.network_inventory.repository.SplitterRepository;
import com.company.network_inventory.service.FiberDropLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FiberDropLineServiceImpl implements FiberDropLineService {

    private final FiberDropLineRepository fiberDropLineRepository;
    private final SplitterRepository splitterRepository;
    private final CustomerRepository customerRepository;
    private final AuditService auditService;

    @Override
    public FiberDropLine create(FiberDropLine request) {

        if (request.getFromSplitter() == null || request.getFromSplitter().getSplitterId() == null) {
            throw new IllegalArgumentException("fromSplitter.splitterId is required");
        }
        if (request.getToCustomer() == null || request.getToCustomer().getCustomerId() == null) {
            throw new IllegalArgumentException("toCustomer.customerId is required");
        }

        Splitter splitter = splitterRepository.findById(request.getFromSplitter().getSplitterId())
                .orElseThrow(() -> new ResourceNotFoundException("Splitter not found: " + request.getFromSplitter().getSplitterId()));

        Customer customer = customerRepository.findById(request.getToCustomer().getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + request.getToCustomer().getCustomerId()));

        request.setFromSplitter(splitter);
        request.setToCustomer(customer);

        FiberDropLine saved = fiberDropLineRepository.save(request);

        auditService.log(
                "CREATE",
                "FIBER_DROP_LINE",
                saved.getLineId(), // make sure getter exists in entity
                "Created fiber line from splitterId=" + splitter.getSplitterId() + " to customerId=" + customer.getCustomerId()
        );

        return saved;
    }

    @Override
    public List<FiberDropLine> getAll() {
        return fiberDropLineRepository.findAll();
    }
}
