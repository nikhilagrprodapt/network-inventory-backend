package com.company.network_inventory.service.impl;

import com.company.network_inventory.audit.service.AuditService;
import com.company.network_inventory.entity.CoreSwitch;
import com.company.network_inventory.entity.Headend;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.CoreSwitchRepository;
import com.company.network_inventory.repository.HeadendRepository;
import com.company.network_inventory.service.CoreSwitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreSwitchServiceImpl implements CoreSwitchService {

    private final CoreSwitchRepository coreSwitchRepository;
    private final HeadendRepository headendRepository;
    private final AuditService auditService;

    @Override
    public CoreSwitch create(CoreSwitch request) {
        if (request.getHeadend() != null && request.getHeadend().getHeadendId() != null) {
            Headend headend = headendRepository.findById(request.getHeadend().getHeadendId())
                    .orElseThrow(() -> new ResourceNotFoundException("Headend not found: " + request.getHeadend().getHeadendId()));
            request.setHeadend(headend);
        }

        CoreSwitch saved = coreSwitchRepository.save(request);

        auditService.log(
                "CREATE",
                "CORE_SWITCH",
                saved.getCoreSwitchId(), // make sure getter exists in entity
                "Created core switch name=" + saved.getName()
        );

        return saved;
    }

    @Override
    public List<CoreSwitch> getAll() {
        return coreSwitchRepository.findAll();
    }
}
