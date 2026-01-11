package com.company.network_inventory.repository;

import com.company.network_inventory.entity.Asset;
import com.company.network_inventory.entity.enums.AssetStatus;
import com.company.network_inventory.entity.enums.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    // ✅ Used by AssetServiceImpl (required)
    boolean existsBySerialNumber(String serialNumber);

    // Existing queries
    List<Asset> findByType(AssetType type);

    List<Asset> findByStatus(AssetStatus status);

    List<Asset> findByTypeAndStatus(AssetType type, AssetStatus status);

    // ✅ Journey 4: find assets currently assigned to a customer
    List<Asset> findByAssignedToCustomer_CustomerId(Long customerId);
}
