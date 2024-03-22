package kz.logitex.kartoteka.secret;

import jakarta.validation.Valid;
import kz.logitex.kartoteka.exception.ForbiddenException;
import kz.logitex.kartoteka.model.Role;
import kz.logitex.kartoteka.model.Secret;
import kz.logitex.kartoteka.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/secret")
@RequiredArgsConstructor
public class SecretController {
    @Autowired
    private SecretService secretService;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping()
    public ResponseEntity<?> createSecret(@Valid @RequestBody Secret secret) {
        if (!authUtil.hasPrivileges(List.of(Role.ADMIN))) throw new ForbiddenException("Доступ запрещен");
        return ResponseEntity.ok(secretService.createSecret(secret));
    }

    @GetMapping
    public ResponseEntity<?> getAllSecrets() {
        return ResponseEntity.ok(secretService.getAllSecrets());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSecret(@PathVariable(value = "id") Long id, @Valid @RequestBody Secret secret) {
        if (!authUtil.hasPrivileges(List.of(Role.ADMIN))) throw new ForbiddenException("Доступ запрещен");
        return ResponseEntity.ok(secretService.updateSecret(id, secret));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSecretById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(secretService.getSecretById(id));
    }
}
