package kz.logitex.kartoteka.repository;

import jakarta.persistence.EntityManager;
import kz.logitex.kartoteka.model.Building;
import kz.logitex.kartoteka.util.StringModifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BuildingSearchDaoImpl implements BuildingSearchDao {
    private final EntityManager em;

    @Override
    public List<Building> searchBuilding(String term) {
        var criteriaBuilder = em.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(Building.class);
        var root = criteriaQuery.from(Building.class);
        criteriaQuery.select(root);

        if (term != null && !term.isEmpty()) {
            var normalizedTerm = StringModifier.normalizeAndLowerCase(term);
            var predicate = criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + normalizedTerm + "%"
                    )
            );

            criteriaQuery.where(predicate);
        }

        var query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }
}

