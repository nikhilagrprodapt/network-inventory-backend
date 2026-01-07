package com.company.network_inventory.repository;

import com.company.network_inventory.entity.AssetAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetAssignmentRepository extends JpaRepository<AssetAssignment, Long> {

    Optional<AssetAssignment> findFirstByAsset_AssetIdAndUnassignedAtIsNullOrderByAssignedAtDesc(Long assetId);

    List<AssetAssignment> findByAsset_AssetIdOrderByAssignedAtDesc(Long assetId);
}
