package com.company.network_inventory.service.impl;

import com.company.network_inventory.audit.service.AuditService;
import com.company.network_inventory.dto.AssetAssignRequest;
import com.company.network_inventory.dto.AssetResponse;
import com.company.network_inventory.dto.CustomerCreateRequest;
import com.company.network_inventory.dto.CustomerResponse;
import com.company.network_inventory.dto.onboarding.OnboardingConfirmRequest;
import com.company.network_inventory.dto.onboarding.OnboardingConfirmResponse;
import com.company.network_inventory.dto.task.TaskCreateRequest;
import com.company.network_inventory.dto.task.TaskResponse;
import com.company.network_inventory.entity.FDH;
import com.company.network_inventory.entity.Splitter;
import com.company.network_inventory.entity.enums.AssetType;
import com.company.network_inventory.entity.enums.ConnectionType;
import com.company.network_inventory.entity.enums.CustomerStatus;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.CustomerRepository;
import com.company.network_inventory.repository.FDHRepository;
import com.company.network_inventory.repository.SplitterRepository;
import com.company.network_inventory.service.AssetService;
import com.company.network_inventory.service.CustomerService;
import com.company.network_inventory.service.DeploymentTaskService;
import com.company.network_inventory.service.OnboardingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class OnboardingServiceImpl implements OnboardingService {

    private final FDHRepository fdhRepository;
    private final SplitterRepository splitterRepository;
    private final CustomerRepository customerRepository;

    private final CustomerService customerService;
    private final AssetService assetService;
    private final DeploymentTaskService deploymentTaskService;

    private final AuditService auditService;

    @Override
    public List<FDH> getAllFdhs() {
        return fdhRepository.findAll();
    }

    @Override
    public List<Splitter> getSplittersByFdh(Long fdhId) {
        if (fdhId == null || !fdhRepository.existsById(fdhId)) {
            throw new ResourceNotFoundException("FDH not found: " + fdhId);
        }
        return splitterRepository.findByFdh_FdhId(fdhId);
    }

    @Override
    public List<Integer> getFreePorts(Long splitterId) {
        Splitter splitter = splitterRepository.findById(splitterId)
                .orElseThrow(() -> new ResourceNotFoundException("Splitter not found: " + splitterId));

        int cap = splitter.getPortCapacity();

        Set<Integer> used = new HashSet<>();
        customerRepository.findBySplitter_SplitterId(splitterId).forEach(c -> {
            if (c.getSplitterPort() != null) used.add(c.getSplitterPort());
        });

        return IntStream.rangeClosed(1, cap)
                .filter(p -> !used.contains(p))
                .boxed()
                .toList();
    }

    @Override
    @Transactional
    public OnboardingConfirmResponse confirm(OnboardingConfirmRequest request) {

        // 1) validate FDH + Splitter
        FDH fdh = fdhRepository.findById(request.getFdhId())
                .orElseThrow(() -> new ResourceNotFoundException("FDH not found: " + request.getFdhId()));

        Splitter splitter = splitterRepository.findById(request.getSplitterId())
                .orElseThrow(() -> new ResourceNotFoundException("Splitter not found: " + request.getSplitterId()));

        if (splitter.getFdh() == null || !Objects.equals(splitter.getFdh().getFdhId(), fdh.getFdhId())) {
            throw new IllegalArgumentException("Splitter does not belong to selected FDH.");
        }

        // 2) validate port
        Integer portObj = request.getSplitterPort();
        if (portObj == null) throw new IllegalArgumentException("splitterPort is required.");
        int port = portObj;

        if (port < 1 || port > splitter.getPortCapacity()) {
            throw new IllegalArgumentException("Invalid port. Splitter supports ports 1 to " + splitter.getPortCapacity());
        }

        boolean taken = customerRepository.existsBySplitter_SplitterIdAndSplitterPort(splitter.getSplitterId(), port);
        if (taken) throw new IllegalArgumentException("Port " + port + " is already assigned on this splitter.");

        // 3) create customer as PENDING
        CustomerCreateRequest cr = new CustomerCreateRequest();
        cr.setName(request.getName());
        cr.setAddress(request.getAddress());
        cr.setNeighborhood(request.getNeighborhood());
        cr.setPlan(request.getPlan());
        cr.setConnectionType(ConnectionType.valueOf(request.getConnectionType().trim().toUpperCase()));
        cr.setStatus(CustomerStatus.PENDING);
        cr.setSplitterId(splitter.getSplitterId());
        cr.setSplitterPort(port);

        CustomerResponse customer = customerService.createCustomer(cr);

        // 4) assign ONT
        AssetAssignRequest ontAssign = new AssetAssignRequest();
        ontAssign.setCustomerId(customer.getCustomerId());
        AssetResponse ont = assetService.assignAsset(request.getOntAssetId(), ontAssign);

        if (ont.getType() != AssetType.ONT) {
            throw new IllegalArgumentException("Selected ONT asset is not type ONT.");
        }

        // 5) assign ROUTER
        AssetAssignRequest routerAssign = new AssetAssignRequest();
        routerAssign.setCustomerId(customer.getCustomerId());
        AssetResponse router = assetService.assignAsset(request.getRouterAssetId(), routerAssign);

        if (router.getType() != AssetType.ROUTER) {
            throw new IllegalArgumentException("Selected Router asset is not type ROUTER.");
        }

        // 6) create deployment task
        TaskCreateRequest tr = new TaskCreateRequest();
        tr.setCustomerId(customer.getCustomerId());
        tr.setTaskType(request.getTaskType());
        tr.setNotes(request.getNotes());

        // only set if your TaskCreateRequest allows it (nullable). If not, keep null.
        if (request.getTechnicianId() != null) {
            tr.setTechnicianId(request.getTechnicianId());
        }

        TaskResponse task = deploymentTaskService.create(tr);

        // 7) summary audit
        auditService.log(
                "ONBOARDING_CONFIRMED",
                "CUSTOMER",
                customer.getCustomerId(),
                "FDH=" + safe(fdh.getName()) +
                        ", SplitterId=" + splitter.getSplitterId() +
                        ", Port=" + port +
                        ", ONT=" + ont.getAssetId() +
                        ", ROUTER=" + router.getAssetId() +
                        ", TaskId=" + task.getTaskId()
        );

        OnboardingConfirmResponse resp = new OnboardingConfirmResponse();
        resp.setCustomer(customer);
        resp.setOnt(ont);
        resp.setRouter(router);
        resp.setTask(task);
        return resp;
    }

    private String safe(String s) {
        return (s == null || s.isBlank()) ? "-" : s.trim();
    }
}
