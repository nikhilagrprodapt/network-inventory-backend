package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.topology.*;
import com.company.network_inventory.entity.*;
import com.company.network_inventory.entity.enums.AssetType;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.*;
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
    private final FiberDropLineRepository fiberDropLineRepository;
    private final AssetAssignmentRepository assetAssignmentRepository;
    private final AssetRepository assetRepository;


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

                List<Customer> customers =
                        customerRepository.findBySplitter_SplitterId(splitter.getSplitterId());

                List<TopologyCustomerNode> customerNodes = customers.stream()
                        .map(this::toCustomerNode)
                        .toList();

                List<FiberDropLine> lines =
                        fiberDropLineRepository.findByFromSplitter_SplitterId(splitter.getSplitterId());

                List<TopologyFiberDropLineNode> lineNodes = lines.stream()
                        .map(this::toFiberLineNode)
                        .toList();

                return TopologySplitterNode.builder()
                        .splitterId(splitter.getSplitterId())
                        .name(splitter.getName())
                        .model(splitter.getModel())
                        .portCapacity(splitter.getPortCapacity())
                        .customers(customerNodes)
                        .fiberDropLines(lineNodes)
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

    @Override
    public TopologyCustomerDetailsResponse getCustomerDetails(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));

        Splitter splitter = customer.getSplitter();

        FiberDropLine fiber =
                fiberDropLineRepository.findByToCustomer_CustomerId(customerId).orElse(null);

        String ontSerial = null;
        String ontStatus = null;
        String routerSerial = null;
        String routerStatus = null;

        List<AssetAssignment> assignments =
                assetAssignmentRepository.findByCustomer_CustomerIdAndUnassignedAtIsNull(customerId);

        if (assignments != null && !assignments.isEmpty()) {
            for (AssetAssignment aa : assignments) {
                Asset asset = aa.getAsset();
                if (asset == null) continue;

                if (asset.getType() == AssetType.ONT) {
                    ontSerial = asset.getSerialNumber();
                    ontStatus = asset.getStatus().name();
                } else if (asset.getType() == AssetType.ROUTER) {
                    routerSerial = asset.getSerialNumber();
                    routerStatus = asset.getStatus().name();
                }
            }
        } else {

            List<Asset> currentAssets = assetRepository.findByAssignedToCustomer_CustomerId(customerId);
            for (Asset asset : currentAssets) {
                if (asset == null) continue;

                if (asset.getType() == AssetType.ONT) {
                    ontSerial = asset.getSerialNumber();
                    ontStatus = asset.getStatus().name();
                } else if (asset.getType() == AssetType.ROUTER) {
                    routerSerial = asset.getSerialNumber();
                    routerStatus = asset.getStatus().name();
                }
            }
        }

        return TopologyCustomerDetailsResponse.builder()
                .customerId(customer.getCustomerId())
                .name(customer.getName())
                .address(customer.getAddress())
                .status(customer.getStatus() != null ? customer.getStatus().name() : null)
                .splitterId(splitter != null ? splitter.getSplitterId() : null)
                .splitterName(splitter != null ? splitter.getName() : null)
                .splitterPort(customer.getSplitterPort())
                .fiberLineId(fiber != null ? fiber.getLineId() : null)
                .fiberStatus(fiber != null && fiber.getStatus() != null ? fiber.getStatus().name() : null)
                .fiberLengthMeters(fiber != null ? fiber.getLengthMeters() : null)
                .ontSerial(ontSerial)
                .ontStatus(ontStatus)
                .routerSerial(routerSerial)
                .routerStatus(routerStatus)
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
            customerNode = toCustomerNode(line.getToCustomer());
        }

        return TopologyFiberDropLineNode.builder()
                .lineId(line.getLineId())
                .lengthMeters(line.getLengthMeters())
                .status(line.getStatus() != null ? line.getStatus().name() : null)
                .customer(customerNode)
                .build();
    }
}
