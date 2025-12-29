package com.company.network_inventory.controller;

import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.FiberDropLine;
import com.company.network_inventory.entity.Splitter;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.CustomerRepository;
import com.company.network_inventory.repository.FiberDropLineRepository;
import com.company.network_inventory.repository.SplitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fiber-lines")
@RequiredArgsConstructor
public class FiberDropLineController {

    private final FiberDropLineRepository repo;
    private final SplitterRepository splitterRepository;
    private final CustomerRepository customerRepository;

    @PostMapping
    public FiberDropLine create(@RequestBody FiberDropLine request) {

        Splitter splitter = splitterRepository.findById(request.getFromSplitter().getSplitterId())
                .orElseThrow(() -> new ResourceNotFoundException("Splitter not found"));

        Customer customer = customerRepository.findById(request.getToCustomer().getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        request.setFromSplitter(splitter);
        request.setToCustomer(customer);

        return repo.save(request);
    }

    @GetMapping
    public List<FiberDropLine> all() {
        return repo.findAll();
    }
}
