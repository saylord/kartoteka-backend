package kz.logitex.kartoteka.util;

import kz.logitex.kartoteka.model.Auth;
import kz.logitex.kartoteka.model.Role;
import kz.logitex.kartoteka.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthUtil {
    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves the authenticated user's authentication details.
     *
     * @return The Auth object representing the authenticated user.
     */
    public Auth getAuth() {
        return (Auth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean hasPrivileges(List<Role> rolesToCheck) {
        var auth = getAuth();
        var user = userRepository.findById(auth.getUserId()).orElse(null);
        if (user != null) {
            var userRole = user.getRole();
            return rolesToCheck.contains(userRole);
        }
        return false;
    }
}

