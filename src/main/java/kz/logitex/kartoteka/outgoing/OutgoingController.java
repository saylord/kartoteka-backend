package kz.logitex.kartoteka.outgoing;

import jakarta.validation.Valid;
import kz.logitex.kartoteka.exception.ForbiddenException;
import kz.logitex.kartoteka.ingoing.IngoingService;
import kz.logitex.kartoteka.model.Ingoing;
import kz.logitex.kartoteka.model.Outgoing;
import kz.logitex.kartoteka.model.Role;
import kz.logitex.kartoteka.model.Status;
import kz.logitex.kartoteka.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/outgoing")
@RequiredArgsConstructor
public class OutgoingController {
    @Autowired
    private OutgoingService outgoingService;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping
    public ResponseEntity<?> createOutgoing(@Valid @RequestBody Outgoing request) {
        return ResponseEntity.ok(outgoingService.createOutgoing(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOutgoing(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(outgoingService.getOutgoingById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllOutgoings(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "25") int size,
                                            @RequestParam(required = false) Set<Status> status,
                                            @RequestParam(defaultValue = "id") String sort,
                                            @RequestParam(defaultValue = "false") boolean asc,
                                             @RequestParam(defaultValue = "2024") int year,
                                            @RequestParam(required = false) String term) {
        return ResponseEntity.ok(outgoingService.getAllOutgoingsByStatus(page, size, status, sort, asc, year, term));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOutgoing(@PathVariable(value = "id") Long id, @RequestBody Outgoing outgoing) {
        return ResponseEntity.ok(outgoingService.updateOutgoing(id, outgoing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOutgoing(@PathVariable(value = "id") Long id) {
        if (!authUtil.hasPrivileges(List.of(Role.ADMIN))) throw new ForbiddenException("Доступ запрещен");
        return ResponseEntity.ok(outgoingService.deleteOutgoing(id));
    }
}
