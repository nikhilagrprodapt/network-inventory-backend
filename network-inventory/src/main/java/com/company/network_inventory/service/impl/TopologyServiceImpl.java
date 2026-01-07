package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.topology.*;
import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.FDH;
import com.company.network_inventory.entity.FiberDropLine;
import com.company.network_inventory.entity.Headend;
import com.company.network_inventory.entity.Splitter;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.CustomerRepository;
import com.company.network_inventory.repository.FDHRepository;
import com.company.network_inventory.repository.FiberDropLineRepository;
import com.company.network_inventory.repository.HeadendRepository;
import com.company.network_inventory.repository.SplitterRepository;
import com.company.network_inventory.service.TopologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopologyServiceImpl implements TopologyService {

    private final HeadendRepository headendRepository;
    private final FDHRepository fdhRepository;
    private final SplitterRepository splitterRepository;
    private final CustomerRepository customerRepository;

    // NEW
    private final FiberDropLineRepository fiberDropLineRepository;

    @Override
    public List<Headend> listHeadends() {
        return headendRepository.findAll();
    }

    @Override
    public TopologyResponse getTopology(Long headendId) {
        Headend headend = headendRepository.findById(headendId)
                .orElseThrow(() -> new ResourceNotFoundException("Headend not found: " + headendId));

        List<FDH> fdhs = fdhRepository.findByHeadend_HeadendId(headendId);

        List<TopologyFDHNode> fdhNodes = fdhs.stream().map(fdh -> {
            List<Splitter> splitters = splitterRepository.findByFdh_FdhId(fdh.getFdhId());

            List<TopologySplitterNode> splitterNodes = splitters.stream().map(splitter -> {

                // Existing: customers directly under splitter (kept, optional)
                List<Customer> customers = customerRepository.findBySplitter_SplitterId(splitter.getSplitterId());
                List<TopologyCustomerNode> customerNodes = customers.stream()
                        .map(this::toCustomerNode)
                        .toList();

                // NEW: fiber lines under splitter
                List<FiberDropLine> lines = fiberDropLineRepository.findByFromSplitter_SplitterId(splitter.getSplitterId());
                List<TopologyFiberDropLineNode> lineNodes = lines.stream()
                        .map(this::toFiberLineNode)
                        .toList();

                return TopologySplitterNode.builder()
                        .splitterId(splitter.getSplitterId())
                        .name(splitter.getName())
                        .model(splitter.getModel())
                        .portCapacity(splitter.getPortCapacity())
                        .customers(customerNodes)          // keep for compatibility
                        .fiberDropLines(lineNodes)         // new structure
                        .build();
            }).toList();

            return TopologyFDHNode.builder()
                    .fdhId(fdh.getFdhId())
                    .name(fdh.getName())
                    .region(fdh.getRegion())
                    .location(fdh.getLocation())
                    .splitters(splitterNodes)
                    .build();
        }).toList();

        return TopologyResponse.builder()
                .headendId(headend.getHeadendId())
                .headendName(headend.getName())
                .headendLocation(headend.getLocation())
                .fdhs(fdhNodes)
                .build();
    }

    private TopologyCustomerNode toCustomerNode(Customer c) {
        return TopologyCustomerNode.builder()
                .customerId(c.getCustomerId())
                .name(c.getName())
                .splitterPort(c.getSplitterPort())
                .status(c.getStatus() != null ? c.getStatus().name() : null)
                .build();
    }

    private TopologyFiberDropLineNode toFiberLineNode(FiberDropLine line) {
        TopologyCustomerNode customerNode = null;
        if (line.getToCustomer() != null) {
            Customer c = line.getToCustomer();
            customerNode = toCustomerNode(c);
        }

        return TopologyFiberDropLineNode.builder()
                .lineId(line.getLineId())
                .lengthMeters(line.getLengthMeters())
                .status(line.getStatus() != null ? line.getStatus().name() : null)
                .customer(customerNode)
                .build();
    }
}
