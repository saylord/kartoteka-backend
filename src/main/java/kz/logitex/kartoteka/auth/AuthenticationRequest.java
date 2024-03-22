package kz.logitex.kartoteka.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Represents an authentication request with username and password.
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthenticationRequest {
    private String username;    // User's username (email).
    private String password;    // User's password.
}
