package com.cvsu.cvsu_api.service;

import com.cvsu.cvsu_api.entity.RoomEntity;
import com.cvsu.cvsu_api.model.BuildingInfoModel;
import com.cvsu.cvsu_api.model.ResponseModel;

import java.util.List;

public interface BuildingAndRoomsService {
   ResponseModel addNewBldgAndRoom(BuildingInfoModel info);
   ResponseModel updateBldgAndRoom(BuildingInfoModel newInfo);
   ResponseModel deleteBldg(Long id);
   ResponseModel editRoom(RoomEntity newRoomInfo);
   ResponseModel deleteRoom(Long id);
   List<BuildingInfoModel> displayAllBldgAndRms();

   BuildingInfoModel displayBldgAndRms(String buildingName);

}
