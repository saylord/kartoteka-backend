package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByUsername(String username);
    Optional<Auth> findByUserId(Long id);
}
