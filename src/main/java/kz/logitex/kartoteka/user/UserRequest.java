package kz.logitex.kartoteka.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kz.logitex.kartoteka.model.Role;
import lombok.Data;

/**
 * Request object for creating a user.
 */
@Data
public class UserRequest {
    @NotBlank(message = "Неверное имя: пустое имя")
    @NotNull(message = "Неверное имя: имя NULL")
    private String firstname;
    @NotBlank(message = "Неверная фамилия: пустая фамилия")
    @NotNull(message = "Неверная фамилия: фамилия NULL")
    private String lastname;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean active = true;
}
