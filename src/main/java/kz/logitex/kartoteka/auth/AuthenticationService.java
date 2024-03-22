package kz.logitex.kartoteka.auth;

import kz.logitex.kartoteka.configuration.JwtService;
import kz.logitex.kartoteka.exception.BadRequestException;
import kz.logitex.kartoteka.exception.NotFoundException;
import kz.logitex.kartoteka.repository.AuthRepository;
import kz.logitex.kartoteka.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for authentication-related operations.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param request The authentication request.
     * @return The generated AuthenticationResponse.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var auth = authRepository.findByUsername(request.getUsername()).orElseThrow(() -> new NotFoundException("Аутентификация не найдена с именем пользователя: " + request.getUsername()));
        var user = userRepository.findById(auth.getUserId()).orElseThrow(() -> new NotFoundException("Пользователь не найден с именем пользователя: " + request.getUsername()));
        if (!user.isActive())
            throw new BadRequestException("Имя пользователя '" + request.getUsername() + "' не активен, и не имеет доступа к входу.");
        var jwtToken = jwtService.generateToken(auth);
        return AuthenticationResponse.builder()
                .id(auth.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(auth.getUsername())
                .role(user.getRole())
                .token(jwtToken)
                .userId(auth.getUserId())
                .build();
    }
}
