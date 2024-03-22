package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.Secret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecretRepository extends JpaRepository<Secret, Long> {
    Optional<Secret> findByName(String name);
}
