package kz.logitex.kartoteka.ingoing;

import jakarta.validation.Valid;
import kz.logitex.kartoteka.exception.ForbiddenException;
import kz.logitex.kartoteka.model.Ingoing;
import kz.logitex.kartoteka.model.Role;
import kz.logitex.kartoteka.model.Status;
import kz.logitex.kartoteka.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Controller class responsible for handling HTTP requests related to ingoings.
 */
@RestController
@RequestMapping("/api/v1/ingoing")
@RequiredArgsConstructor
public class IngoingController {
    @Autowired
    private IngoingService ingoingService;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping
    public ResponseEntity<?> createIngoing(@Valid @RequestBody Ingoing request) {
        return ResponseEntity.ok(ingoingService.createIngoing(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIngoing(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(ingoingService.getIngoingById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllIngoings(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "25") int size,
                                            @RequestParam(required = false) Set<Status> status,
                                            @RequestParam(defaultValue = "id") String sort,
                                            @RequestParam(defaultValue = "false") boolean asc,
                                            @RequestParam(defaultValue = "2024") int year,
                                            @RequestParam(required = false) String term) {
        return ResponseEntity.ok(ingoingService.getAllIngoingsByStatus(page, size, status, sort, asc, year, term));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateIngoing(@PathVariable(value = "id") Long id, @RequestBody Ingoing ingoing) {
        return ResponseEntity.ok(ingoingService.updateIngoing(id, ingoing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIngoing(@PathVariable(value = "id") Long id) {
        if (!authUtil.hasPrivileges(List.of(Role.ADMIN))) throw new ForbiddenException("Доступ запрещен");
        return ResponseEntity.ok(ingoingService.deleteIngoing(id));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchIngoings(@RequestParam String term) {
        return ResponseEntity.ok(ingoingService.searchIngoings(term));
    }
}
