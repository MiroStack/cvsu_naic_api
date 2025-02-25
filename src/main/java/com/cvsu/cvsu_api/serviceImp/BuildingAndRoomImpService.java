package com.cvsu.cvsu_api.serviceImp;

import com.cvsu.cvsu_api.entity.BuildingEntity;
import com.cvsu.cvsu_api.entity.RoomEntity;
import com.cvsu.cvsu_api.model.BuildingInfoModel;
import com.cvsu.cvsu_api.model.ResponseModel;
import com.cvsu.cvsu_api.repository.BuildingRepository;
import com.cvsu.cvsu_api.repository.RoomRepository;
import com.cvsu.cvsu_api.service.BuildingAndRoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BuildingAndRoomImpService implements BuildingAndRoomsService {
 @Autowired
    BuildingRepository buildingRepository;

 @Autowired
    RoomRepository roomRepository;

    @Override
    public ResponseModel addNewBldgAndRoom(BuildingInfoModel info) {
        ResponseModel res = new ResponseModel();
        BuildingEntity buildingEntity = new BuildingEntity();

        try{
            buildingEntity.setBuildingName(info.getBuildingName());
            buildingEntity.setBuildingDetails(info.getBuildingDetails());

            BuildingEntity addedBuilding = buildingRepository.save(buildingEntity);

            for(RoomEntity entity : info.getRoomsInfo()){
                RoomEntity roomEntity = new RoomEntity();
                roomEntity.setBuildingId(addedBuilding.getId());
                roomEntity.setRoomName(entity.getRoomName());
                roomEntity.setFloorId(entity.getFloorId());
                roomRepository.save(roomEntity);
            }


            res.setMessage("New Buildings added successfully with Building name " + addedBuilding.getBuildingName());
            res.setSuccess(true);
            res.setStatusCode(200);

        }
        catch(Exception e){
            res.setMessage("Error occured: "+e );
            res.setSuccess(true);
            res.setStatusCode(500);
        }

        return res;
    }

    @Override
    public ResponseModel updateBldgAndRoom(BuildingInfoModel newInfo) {
        ResponseModel res = new ResponseModel();
        System.out.println(newInfo.getBuildingId());

        try {
            Optional<BuildingEntity> buildingEntityOptional = buildingRepository.findById(newInfo.getBuildingId());
            if (buildingEntityOptional.isPresent()) {
                BuildingEntity buildingEntity = buildingEntityOptional.get();
                buildingEntity.setBuildingName(newInfo.getBuildingName());
                buildingEntity.setBuildingDetails(newInfo.getBuildingDetails());
                BuildingEntity updatedBuildingInfo = buildingRepository.save(buildingEntity);

                // 1️⃣ Get all current rooms from DB
                List<RoomEntity> existingRooms = roomRepository.findByBuildingId(updatedBuildingInfo.getId());

                // 2️⃣ Get new room IDs from the request
                Set<Long> newRoomIds = newInfo.getRoomsInfo().stream()
                        .map(RoomEntity::getRoomId)
                        .filter(Objects::nonNull) // Ignore new rooms (they don't have an ID)
                        .collect(Collectors.toSet());

                // 3️⃣ Delete rooms that exist in the DB but are missing in the new request
                for (RoomEntity existingRoom : existingRooms) {
                    if (!newRoomIds.contains(existingRoom.getRoomId())) {
                        roomRepository.delete(existingRoom);
                    }
                }

                // 4️⃣ Insert or update rooms from request
                for (RoomEntity entity : newInfo.getRoomsInfo()) {
                    if (entity.getRoomId() != null) { // ✅ Update existing room
                        Optional<RoomEntity> roomEntityOptional = roomRepository.findById(entity.getRoomId());
                        if (roomEntityOptional.isPresent()) {
                            RoomEntity roomEntity = roomEntityOptional.get();
                            roomEntity.setRoomName(entity.getRoomName());
                            roomEntity.setBuildingId(updatedBuildingInfo.getId());
                            roomEntity.setFloorId(entity.getFloorId());
                            roomRepository.save(roomEntity);
                        }
                    } else { // ✅ Insert new room
                        RoomEntity newRoom = new RoomEntity();
                        newRoom.setRoomName(entity.getRoomName());
                        newRoom.setBuildingId(updatedBuildingInfo.getId());
                        newRoom.setFloorId(entity.getFloorId());
                        roomRepository.save(newRoom);
                    }
                }
            }

            res.setMessage("Building information has been updated successfully.");
            res.setSuccess(true);
            res.setStatusCode(200);
        } catch (Exception e) {
            res.setMessage("Error occurred: " + e);
            res.setSuccess(false);
            res.setStatusCode(500);
        }
        return res;
    }

    @Override
    public ResponseModel deleteBldg(Long id) {
        ResponseModel res = new ResponseModel();
        System.out.println("id "+id); // not prints
        if (id == null) {
            System.out.println("Error: id is NULL!"); // Debug log
            res.setMessage("Error: Building ID is null.");
            res.setSuccess(false);
            res.setStatusCode(400);
            return res;
        }
        try{
            //delete all rooms base on building id.
            Optional<BuildingEntity> buildingEntityOptional = buildingRepository.findById(id);
            if(buildingEntityOptional.isPresent()){
                BuildingEntity buildingEntity = buildingEntityOptional.get();
                System.out.println(buildingEntity.getBuildingName()); // not prints
                List<RoomEntity> rooms = roomRepository.findByBuildingId(buildingEntity.getId());
                if (!rooms.isEmpty()) {
                    roomRepository.deleteAll(rooms);
                    roomRepository.flush(); // Ensure deletion happens before deleting the building
                }
                buildingRepository.deleteById(buildingEntity.getId());
                res.setMessage("Building "+ buildingEntity.getBuildingName() +" is deleted successfully.");
                res.setStatusCode(200);
                res.setSuccess(true);
            }
            else{
                res.setMessage("Building not found.");
                res.setSuccess(false);
                res.setStatusCode(404);
            }
            return res;


        }catch(Exception e){
            res.setMessage("Error occured: "+e.getMessage());
            res.setSuccess(false);
            res.setStatusCode(500);
            return res;
        }

    }

    @Override
    public ResponseModel editRoom(RoomEntity newRoomInfo) {
        ResponseModel res = new ResponseModel();
        try{
            Optional<RoomEntity> roomEntityOptional = roomRepository.findById(newRoomInfo.getRoomId());
            if(roomEntityOptional.isPresent()){
                RoomEntity roomEntity = roomEntityOptional.get();
                roomEntity.setRoomName(newRoomInfo.getRoomName());
                roomEntity.setFloorId(newRoomInfo.getFloorId());
                roomRepository.save(roomEntity);
                res.setStatusCode(200);
                res.setMessage("Room updated successfully");
                res.setSuccess(true);
            }
            else{
                res.setStatusCode(404);
                res.setMessage("Room not found.");
                res.setSuccess(false);

            }
            return res;
        } catch (Exception e) {
            res.setSuccess(false);
            res.setMessage("Error occured "+e.getMessage());
            res.setStatusCode(500);
            return res;
        }
    }

    @Override
    public ResponseModel deleteRoom(Long id) {
        ResponseModel res = new ResponseModel();
        try{
            Optional<RoomEntity> roomEntityOptional = roomRepository.findById(id);
            if(roomEntityOptional.isPresent()){
                RoomEntity roomEntity = roomEntityOptional.get();
                roomRepository.delete(roomEntity);
                res.setSuccess(true);
                res.setStatusCode(200);
                res.setMessage("Room deleted Successfully");

            }else{
                res.setSuccess(false);
                res.setMessage("Room not found");
                res.setStatusCode(404);
            }
            return res;
        }catch (Exception e){
            res.setSuccess(false);
            res.setMessage("Error occured "+e.getMessage());
            res.setStatusCode(500);
            return res;
        }
    }


    @Override
    public List<BuildingInfoModel> displayAllBldgAndRms() {
        try{
            List<BuildingInfoModel> buildingInfoModelList = new ArrayList<>();
            List<BuildingEntity> buildingEntities = buildingRepository.findAll();
            for(BuildingEntity entity : buildingEntities){
                BuildingInfoModel buildingInfoModel = new BuildingInfoModel();
                List<RoomEntity> roomEntity = roomRepository.findByBuildingId(entity.getId());
                buildingInfoModel.setBuildingId(entity.getId());
                buildingInfoModel.setBuildingName(entity.getBuildingName());
                buildingInfoModel.setBuildingDetails(entity.getBuildingDetails());
                buildingInfoModel.setRoomsInfo(roomEntity);
                buildingInfoModelList.add(buildingInfoModel);
            }
            return buildingInfoModelList;
        }catch(Exception e){
            System.out.print(e.getMessage());
            return List.of();
        }

    }

    @Override
    public BuildingInfoModel displayBldgAndRms(String buildingName) {
        try{
            BuildingInfoModel buildingInfoModel = new BuildingInfoModel();
            BuildingEntity buildingEntity = buildingRepository.findByBuildingName(buildingName);
            if(buildingEntity.getBuildingName().equals(buildingName)){
                buildingInfoModel.setBuildingName(buildingEntity.getBuildingName());
                buildingInfoModel.setBuildingDetails(buildingEntity.getBuildingDetails());
                buildingInfoModel.setBuildingId((buildingEntity.getId()));

                List<RoomEntity> roomList = roomRepository.findByBuildingId(buildingEntity.getId());
                buildingInfoModel.setRoomsInfo(roomList);
                return buildingInfoModel;
            }
            else{
                System.out.print("No building found.");
            }
        }catch(Exception e){
            System.out.print("Error occurred:" + e.getMessage());
        }
        return null;
    }


}
