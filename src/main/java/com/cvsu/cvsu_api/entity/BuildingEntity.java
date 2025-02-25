package com.cvsu.cvsu_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_building")
public class BuildingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    private Long id;

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBuildingDetails() {
        return buildingDetails;
    }

    public void setBuildingDetails(String buildingDetails) {
        this.buildingDetails = buildingDetails;
    }

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "building_details")
    private String buildingDetails;
}
