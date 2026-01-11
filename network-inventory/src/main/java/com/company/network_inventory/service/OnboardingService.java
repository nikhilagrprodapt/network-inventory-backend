package com.company.network_inventory.service;

import com.company.network_inventory.dto.onboarding.OnboardingConfirmRequest;
import com.company.network_inventory.dto.onboarding.OnboardingConfirmResponse;
import com.company.network_inventory.entity.FDH;
import com.company.network_inventory.entity.Splitter;

import java.util.List;

public interface OnboardingService {

    List<FDH> getAllFdhs();

    List<Splitter> getSplittersByFdh(Long fdhId);

    List<Integer> getFreePorts(Long splitterId);

    OnboardingConfirmResponse confirm(OnboardingConfirmRequest request);
}
