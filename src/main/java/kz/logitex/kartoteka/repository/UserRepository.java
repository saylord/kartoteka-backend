package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserSearchDao {
}
