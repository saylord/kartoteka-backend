package kz.logitex.kartoteka.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import kz.logitex.kartoteka.model.Role;
import kz.logitex.kartoteka.model.User;
import kz.logitex.kartoteka.util.StringModifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserSearchDaoImpl implements UserSearchDao {
    private final EntityManager em;

    @Override
    public List<User> searchUsersByFirstNameOrLastName(String term, Role role) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(User.class);
        var root = query.from(User.class);

        var predicates = new ArrayList<Predicate>();

        // Нормализация и преобразование в нижний регистр для русских слов
        var normalizedTerm = StringModifier.normalizeAndLowerCase(term);

        // Создание отдельных предикатов для поиска по firstname и lastname с учетом роли
        var firstNamePredicate = cb.and(
                cb.like(cb.lower(root.get("firstname")), "%" + normalizedTerm + "%"),
                cb.equal(root.get("role"), role),
                cb.isTrue(root.get("active"))
        );

        var lastNamePredicate = cb.and(
                cb.like(cb.lower(root.get("lastname")), "%" + normalizedTerm + "%"),
                cb.equal(root.get("role"), role),
                cb.isTrue(root.get("active"))
        );

        var phonePredicate = cb.and(
                cb.like(root.get("phone"), "%" + term + "%"),
                cb.equal(root.get("role"), role),
                cb.isTrue(root.get("active"))
        );

        predicates.add(cb.or(firstNamePredicate, lastNamePredicate, phonePredicate));

        query.where(cb.or(predicates.toArray(new Predicate[0])));

        return em.createQuery(query).getResultList();
    }
}

