package kz.logitex.kartoteka.user;

import jakarta.validation.Valid;
import kz.logitex.kartoteka.exception.ForbiddenException;
import kz.logitex.kartoteka.model.Role;
import kz.logitex.kartoteka.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for user-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping()
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest request) {
        if (!authUtil.hasPrivileges(List.of(Role.ADMIN))) throw new ForbiddenException("Доступ запрещен");
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable(value = "id") Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsersByNameAndRole(@RequestParam String term, @RequestParam Role role) {
        return ResponseEntity.ok(userService.searchByFirstnameOrLastname(term, role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody UserRequest userRequest) {
        if (!authUtil.hasPrivileges(List.of(Role.ADMIN))) throw new ForbiddenException("Доступ запрещен");
        return ResponseEntity.ok(userService.updateUser(userId, userRequest));
    }
}

