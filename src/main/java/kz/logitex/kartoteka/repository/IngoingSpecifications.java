package kz.logitex.kartoteka.repository;

import kz.logitex.kartoteka.model.Building;
import kz.logitex.kartoteka.model.Ingoing;
import kz.logitex.kartoteka.model.Status;
import kz.logitex.kartoteka.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class IngoingSpecifications {
    public static Specification<Ingoing> withFilters(
            Long start,
            Long end,
            List<Building> buildings,
            List<User> author,
            List<Status> status,
            List<User> executor
    ) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();
            if (start != null && end != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("createdTimestamp"), start, end));
            }
            if (buildings != null && !buildings.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("address").in(buildings));
            }
            if (author != null && !author.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("author").in(author));
            }
            if (status != null && !status.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("status").in(status));
            }
            if (executor != null && !executor.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("executor").in(executor));
            }
            return predicate;
        };
    }
}
