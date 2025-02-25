package com.cvsu.cvsu_api.repository;

import com.cvsu.cvsu_api.entity.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<BuildingEntity, Long> {
    BuildingEntity findByBuildingName(String buildingName);
}
