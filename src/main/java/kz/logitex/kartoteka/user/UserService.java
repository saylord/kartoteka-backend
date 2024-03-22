package kz.logitex.kartoteka.user;

import kz.logitex.kartoteka.exception.NotFoundException;
import kz.logitex.kartoteka.model.Role;
import kz.logitex.kartoteka.model.User;
import kz.logitex.kartoteka.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for user-related operations.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(UserRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .active(request.isActive())
                .role(Role.EXECUTOR)
                .build();

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден с айди: " + userId));
    }

    public List<User> searchByFirstnameOrLastname(String term, Role role) {
        return userRepository.searchUsersByFirstNameOrLastName(term, role);
    }

    public User updateUser(Long userId, UserRequest userRequest) {
        var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден с айди: " + userId));
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());
        user.setRole(userRequest.getRole());
        user.setPhone(userRequest.getPhone());
        user.setActive(userRequest.isActive());
        return userRepository.save(user);
    }
}
