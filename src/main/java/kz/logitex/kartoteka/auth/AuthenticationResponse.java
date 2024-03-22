package kz.logitex.kartoteka.auth;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kz.logitex.kartoteka.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Represents an authentication response containing a token.
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthenticationResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String token;
    private Long userId;
}
