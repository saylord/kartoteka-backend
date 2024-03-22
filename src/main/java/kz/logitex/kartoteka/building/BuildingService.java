package kz.logitex.kartoteka.building;

import kz.logitex.kartoteka.exception.BadRequestException;
import kz.logitex.kartoteka.exception.NotFoundException;
import kz.logitex.kartoteka.model.Building;
import kz.logitex.kartoteka.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingService {
    private final BuildingRepository buildingRepository;

    public Building createBuilding(Building request) {
        var existingAddress = buildingRepository.findByName(request.getName());
        if (existingAddress.isPresent())
            throw new BadRequestException("Данный корреспондент уже существует.");
        return buildingRepository.save(request);
    }

    public Building updateAddress(Long id, Building request) {
        var address = buildingRepository.findById(id).orElseThrow(() -> new NotFoundException("Корреспондент не найден с айди: " + id));
        var existingAddress = buildingRepository.findByName(request.getName());
        if (existingAddress.isPresent())
            throw new BadRequestException("Данный корреспондент уже существует.");
        address.setName(request.getName());
        return buildingRepository.save(address);
    }

    public List<Building> getAllBuildings(String term) {
        return buildingRepository.searchBuilding(term);
    }

    public Building getBuildingById(Long id) {
        return buildingRepository.findById(id).orElseThrow(() -> new NotFoundException("Корреспондент не найден с айди: " + id));
    }
}

