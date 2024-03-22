package kz.logitex.kartoteka.building;

import jakarta.validation.Valid;
import kz.logitex.kartoteka.exception.ForbiddenException;
import kz.logitex.kartoteka.model.Building;
import kz.logitex.kartoteka.model.Role;
import kz.logitex.kartoteka.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/building")
@RequiredArgsConstructor
public class BuildingController {
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping()
    public ResponseEntity<?> createAddress(@Valid @RequestBody Building building) {
        if (!authUtil.hasPrivileges(List.of(Role.ADMIN))) throw new ForbiddenException("Доступ запрещен");
        return ResponseEntity.ok(buildingService.createBuilding(building));
    }

    @GetMapping
    public ResponseEntity<?> getAllBuildings(@RequestParam(required = false) String term) {
        return ResponseEntity.ok(buildingService.getAllBuildings(term));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable(value = "id") Long addressId, @Valid @RequestBody Building building) {
        if (!authUtil.hasPrivileges(List.of(Role.ADMIN))) throw new ForbiddenException("Доступ запрещен");
        return ResponseEntity.ok(buildingService.updateAddress(addressId, building));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBuildingById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(buildingService.getBuildingById(id));
    }
}

