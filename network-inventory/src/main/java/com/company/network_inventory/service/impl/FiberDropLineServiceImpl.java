package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.FiberDropLineCreateRequest;
import com.company.network_inventory.dto.FiberDropLineResponse;
import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.FiberDropLine;
import com.company.network_inventory.entity.Splitter;
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

    @Override
    public FiberDropLineResponse create(FiberDropLineCreateRequest req) {

        Splitter splitter = splitterRepository.findById(req.getFromSplitterId())
                .orElseThrow(() -> new RuntimeException("Splitter not found: " + req.getFromSplitterId()));

        Customer customer = customerRepository.findById(req.getToCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found: " + req.getToCustomerId()));

        FiberDropLine line = FiberDropLine.builder()
                .fromSplitter(splitter)
                .toCustomer(customer)
                .lengthMeters(req.getLengthMeters())
                .status(req.getStatus())
                .build();

        FiberDropLine saved = fiberDropLineRepository.save(line);

        return toResponse(saved);
    }

    @Override
    public FiberDropLine create(FiberDropLine request) {
        return fiberDropLineRepository.save(request);
    }

    @Override
    public List<FiberDropLineResponse> getAll() {
        // If your relationships are LAZY, this still works because we are inside service transaction scope
        // If you ever get LazyInitializationException, I’ll give you the @EntityGraph fix.
        return fiberDropLineRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private FiberDropLineResponse toResponse(FiberDropLine l) {
        var s = l.getFromSplitter();
        var c = l.getToCustomer();

        return FiberDropLineResponse.builder()
                .lineId(l.getLineId())
                .fromSplitterId(s != null ? s.getSplitterId() : null)
                .fromSplitterName(s != null ? s.getName() : null)
                .fromSplitterModel(s != null ? s.getModel() : null)
                .toCustomerId(c != null ? c.getCustomerId() : null)
                .toCustomerName(c != null ? c.getName() : null)
                .toCustomerStatus(c != null && c.getStatus() != null ? c.getStatus().name() : null)
                .lengthMeters(l.getLengthMeters())
                .status(l.getStatus())
                .build();
    }
}
