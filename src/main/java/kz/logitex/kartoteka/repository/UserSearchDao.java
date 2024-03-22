package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.Role;
import kz.logitex.kartoteka.model.User;

import java.util.List;

public interface UserSearchDao {
    List<User> searchUsersByFirstNameOrLastName(String term, Role role);
}
