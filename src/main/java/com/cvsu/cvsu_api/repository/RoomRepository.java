package com.cvsu.cvsu_api.repository;

import com.cvsu.cvsu_api.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    List<RoomEntity> findByBuildingId(Long buildingId);
}
