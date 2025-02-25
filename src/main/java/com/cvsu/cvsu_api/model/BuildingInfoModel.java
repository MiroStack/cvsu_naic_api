package com.cvsu.cvsu_api.model;

import com.cvsu.cvsu_api.entity.BuildingEntity;
import com.cvsu.cvsu_api.entity.RoomEntity;

import java.util.List;

public class BuildingInfoModel {


    private Long buildingId;
    private String buildingName;
    private String buildingDetails;
    private List<RoomEntity> roomsInfo;
    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public List<RoomEntity> getRoomsInfo() {
        return roomsInfo;
    }

    public void setRoomsInfo(List<RoomEntity> roomsInfo) {
        this.roomsInfo = roomsInfo;
    }


    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingDetails() {
        return buildingDetails;
    }

    public void setBuildingDetails(String buildingDetails) {
        this.buildingDetails = buildingDetails;
    }
}
