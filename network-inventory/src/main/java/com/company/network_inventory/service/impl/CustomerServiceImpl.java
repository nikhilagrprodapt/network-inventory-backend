package com.company.network_inventory.service.impl;

import com.company.network_inventory.audit.service.AuditService;
import com.company.network_inventory.dto.CustomerAssignSplitterRequest;
import com.company.network_inventory.dto.CustomerCreateRequest;
import com.company.network_inventory.dto.CustomerDeactivateRequest;
import com.company.network_inventory.dto.CustomerResponse;
import com.company.network_inventory.dto.CustomerUpdateRequest;
import com.company.network_inventory.entity.Asset;
import com.company.network_inventory.entity.AssetAssignment;
import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.Splitter;
import com.company.network_inventory.entity.enums.AssetStatus;
import com.company.network_inventory.entity.enums.AssetType;
import com.company.network_inventory.entity.enums.CustomerStatus;
import com.company.network_inventory.entity.enums.FiberLineStatus;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.AssetAssignmentRepository;
import com.company.network_inventory.repository.AssetRepository;
import com.company.network_inventory.repository.CustomerRepository;
import com.company.network_inventory.repository.FiberDropLineRepository;
import com.company.network_inventory.repository.SplitterRepository;
import com.company.network_inventory.service.CustomerService;
import com.company.network_inventory.support.entity.SupportCase;
import com.company.network_inventory.support.enums.SupportCaseStatus;
import com.company.network_inventory.support.repository.SupportCaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final SplitterRepository splitterRepository;
    private final AuditService auditService;

    private final AssetAssignmentRepository assetAssignmentRepository;
    private final AssetRepository assetRepository;
    private final FiberDropLineRepository fiberDropLineRepository;
    private final SupportCaseRepository supportCaseRepository;

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
                .connectionType(request.getConnectionType())
                .status(request.getStatus())
                .splitter(splitter)
                .splitterPort(request.getSplitterPort())
                .build();

        Customer saved = customerRepository.save(customer);

        auditService.log(
                "CREATE",
                "CUSTOMER",
                saved.getCustomerId(),
                "Created customer name=" + saved.getName() + ", neighborhood=" + saved.getNeighborhood()
        );

        return toResponse(saved);
    }

    @Transactional
    @Override
    public CustomerResponse assignSplitter(Long customerId, CustomerAssignSplitterRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));

        Splitter splitter = splitterRepository.findById(request.getSplitterId())
                .orElseThrow(() -> new ResourceNotFoundException("Splitter not found: " + request.getSplitterId()));

        int port = request.getSplitterPort();

        if (port < 1 || port > splitter.getPortCapacity()) {
            throw new IllegalArgumentException("Invalid port. Splitter supports ports 1 to " + splitter.getPortCapacity());
        }

        boolean portTaken = customerRepository
                .existsBySplitter_SplitterIdAndSplitterPortAndCustomerIdNot(splitter.getSplitterId(), port, customerId);

        if (portTaken) {
            throw new IllegalArgumentException("Port " + port + " is already assigned on this splitter.");
        }

        customer.setSplitter(splitter);
        customer.setSplitterPort(port);

        if (customer.getStatus() == CustomerStatus.PENDING) {
            customer.setStatus(CustomerStatus.ACTIVE);
        }

        Customer saved = customerRepository.save(customer);

        auditService.log(
                "ASSIGN",
                "CUSTOMER",
                saved.getCustomerId(),
                "Assigned splitterId=" + splitter.getSplitterId() + ", port=" + port
        );

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
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));
        customerRepository.delete(customer);

        auditService.log(
                "DELETE",
                "CUSTOMER",
                customerId,
                "Deleted customer name=" + customer.getName()
        );
    }

    @Override
    public CustomerResponse updateCustomer(Long customerId, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));

        if (request == null) {
            throw new IllegalArgumentException("Request body is missing");
        }

        if (request.getPlan() != null) customer.setPlan(request.getPlan());
        if (request.getConnectionType() != null) customer.setConnectionType(request.getConnectionType());
        if (request.getStatus() != null) customer.setStatus(request.getStatus());

        Customer saved = customerRepository.save(customer);

        auditService.log(
                "UPDATE",
                "CUSTOMER",
                saved.getCustomerId(),
                "Updated customer fields (plan/connectionType/status)"
        );

        return toResponse(saved);
    }

    @Transactional
    @Override
    public CustomerResponse deactivateCustomer(Long customerId, CustomerDeactivateRequest request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));

        LocalDateTime now = LocalDateTime.now();

        // 1) Mark customer deactivated
        customer.setStatus(CustomerStatus.DEACTIVATED);

        // 2) Free splitter/port assignment (so port becomes reusable)
        customer.setSplitter(null);
        customer.setSplitterPort(null);

        Customer savedCustomer = customerRepository.save(customer);

        // 3A) Close active AssetAssignment rows for this customer (history)
        List<AssetAssignment> activeAssignments =
                assetAssignmentRepository.findByCustomer_CustomerIdAndUnassignedAtIsNull(customerId);

        for (AssetAssignment aa : activeAssignments) {
            aa.setUnassignedAt(now);
            assetAssignmentRepository.save(aa);
        }

        // 3B) Force-clear any assets still assigned to this customer (source-of-truth cleanup)
        List<Asset> assignedAssets = assetRepository.findByAssignedToCustomer_CustomerId(customerId);

        for (Asset asset : assignedAssets) {
            if (asset == null) continue;

            // Journey 4 requires reclaiming ONT + ROUTER
            if (asset.getType() != AssetType.ONT && asset.getType() != AssetType.ROUTER) {
                continue;
            }

            // Close active assignment for this asset if still open
            assetAssignmentRepository
                    .findFirstByAsset_AssetIdAndUnassignedAtIsNullOrderByAssignedAtDesc(asset.getAssetId())
                    .ifPresent(active -> {
                        active.setUnassignedAt(now);
                        assetAssignmentRepository.save(active);
                    });

            asset.setAssignedToCustomer(null);
            asset.setAssignedAt(null);
            asset.setStatus(AssetStatus.AVAILABLE);
            assetRepository.save(asset);
        }

        // 4) Free fiber path: clear toCustomer and mark line as DISCONNECTED
        fiberDropLineRepository.findByToCustomer_CustomerId(customerId).ifPresent(line -> {
            line.setToCustomer(null);
            line.setStatus(FiberLineStatus.DISCONNECTED);
            fiberDropLineRepository.save(line);
        });

        // 5) Create and close support case
        SupportCase sc = SupportCase.builder()
                .customer(savedCustomer)
                .reason(request.getReason())
                .exitNotes(request.getExitNotes())
                .status(SupportCaseStatus.CLOSED)
                .closedAt(now)
                .build();

        SupportCase savedCase = supportCaseRepository.save(sc);

        // 6) Audit log
        auditService.log(
                "DEACTIVATE",
                "CUSTOMER",
                savedCustomer.getCustomerId(),
                "Deactivated customer. reason=" + request.getReason() +
                        ", caseId=" + savedCase.getCaseId()
        );

        return toResponse(savedCustomer);
    }

    private CustomerResponse toResponse(Customer c) {
        return CustomerResponse.builder()
                .customerId(c.getCustomerId())
                .name(c.getName())
                .address(c.getAddress())
                .neighborhood(c.getNeighborhood())
                .plan(c.getPlan())
                .connectionType(c.getConnectionType())
                .status(c.getStatus())
                .splitterId(c.getSplitter() != null ? c.getSplitter().getSplitterId() : null)
                .splitterPort(c.getSplitterPort())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
