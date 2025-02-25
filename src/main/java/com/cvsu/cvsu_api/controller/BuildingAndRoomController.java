package com.cvsu.cvsu_api.controller;

import com.cvsu.cvsu_api.entity.RoomEntity;
import com.cvsu.cvsu_api.model.BuildingInfoModel;
import com.cvsu.cvsu_api.model.ResponseModel;
import com.cvsu.cvsu_api.serviceImp.BuildingAndRoomImpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cvsu")
@CrossOrigin(origins = "http://192.168.100.27:5000/")
public class BuildingAndRoomController {
    @Autowired
    BuildingAndRoomImpService buildingAndRoomImpService;

    @PostMapping("addBuildingAndRooms")
    public ResponseEntity<ResponseModel> addNewBuildingAndRooms(@RequestBody BuildingInfoModel model){
       try{
           return new ResponseEntity<ResponseModel>(buildingAndRoomImpService.addNewBldgAndRoom(model), HttpStatus.OK);
       }
       catch (Exception e){
           return new ResponseEntity<ResponseModel>(HttpStatus.BAD_REQUEST);
       }
    }

    @PutMapping("updateBuildingAndRooms")
    public ResponseEntity<ResponseModel> updatedBuildingAndRooms(@RequestBody BuildingInfoModel model){
        try{
            return new ResponseEntity<ResponseModel>(buildingAndRoomImpService.updateBldgAndRoom(model), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<ResponseModel>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("displayAllBldgAndRooms")
    public ResponseEntity<List<BuildingInfoModel>> displayAllBuildingAndRooms(){
        try{
            return new ResponseEntity<List<BuildingInfoModel>>(buildingAndRoomImpService.displayAllBldgAndRms(), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<List<BuildingInfoModel>>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteBuilding")
    public ResponseEntity<ResponseModel> deleteBuilding(@RequestParam Long buildingId){
        try{
            System.out.println(buildingId);
            return new ResponseEntity<ResponseModel>(buildingAndRoomImpService.deleteBldg(buildingId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseModel>(HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("updateRoom")
    public ResponseEntity<ResponseModel> updateRoom(@RequestBody RoomEntity newRoomInfo){
        try{
            return new ResponseEntity<ResponseModel>(buildingAndRoomImpService.editRoom(newRoomInfo),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<ResponseModel>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteRoom")
    public ResponseEntity<ResponseModel> deleteRoom(@RequestParam Long roomId){
        try{
            return new ResponseEntity<ResponseModel>(buildingAndRoomImpService.deleteRoom(roomId), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<ResponseModel>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("displayBldgAndRooms")
    public ResponseEntity<BuildingInfoModel> displayBldgAndRooms(String buildingName){
        try{
           return new ResponseEntity<BuildingInfoModel>(buildingAndRoomImpService.displayBldgAndRms(buildingName), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<BuildingInfoModel>(HttpStatus.BAD_REQUEST);
        }
    }
}
