package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.Building;

import java.util.List;

public interface BuildingSearchDao {
    List<Building> searchBuilding(String term);
}
