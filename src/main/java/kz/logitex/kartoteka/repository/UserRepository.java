package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserSearchDao {
}
