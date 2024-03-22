package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long>, BuildingSearchDao {
    Optional<Building> findByName(String name);
}
