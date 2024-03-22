package kz.logitex.kartoteka.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import kz.logitex.kartoteka.model.Outgoing;
import kz.logitex.kartoteka.model.Status;
import kz.logitex.kartoteka.outgoing.OutgoingMinDTO;
import kz.logitex.kartoteka.util.StringModifier;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OutgoingSearchDaoImpl implements OutgoingSearchDao {
    private final EntityManager em;

    @Override
    public Pair<Page<OutgoingMinDTO>, List<OutgoingMinDTO>> findByStatusIn(String term, Set<Status> statuses, Pageable pageable) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(OutgoingMinDTO.class);
        var root = query.from(Outgoing.class);

        // Create a list to hold the predicates for filtering
        var predicates = new ArrayList<Predicate>();
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        createOutgoingMinDTOQuery(cb, root, query);
        var listOutgoings = em.createQuery(query);
        // Check if term is not null or empty
        if (term != null && !term.isEmpty()) {
            var id = parseId(term);

            if (id != null) {
                predicates.add(searchById(id, cb, root));
            } else {
                var normalizedTerm = StringModifier.normalizeAndLowerCase(term);
                var searchConditions = new ArrayList<Predicate>();
                searchConditions.add(searchByDocumentNumber(normalizedTerm, cb, root));
                searchConditions.add(searchByBuilding(normalizedTerm, cb, root));
                searchConditions.add(searchBySecret(normalizedTerm, cb, root));
                searchConditions.add(searchByExecutor(normalizedTerm, cb, root));
                var searchCondition = cb.or(searchConditions.toArray(new Predicate[0]));
                predicates.add(searchCondition);
            }
        }

        // Add predicates for filtering by status
        if (statuses != null && !statuses.isEmpty()) {
            predicates.add(getStatusInPredicate(statuses, cb, root));
        }

        // Construct the WHERE clause with the combined predicates
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // Select the necessary attributes to construct TicketMinDTO
        createOutgoingMinDTOQuery(cb, root, query);

        // Apply sorting
        var orderList = pageable.getSort().stream()
                .map(order -> {
                    var path = root.get(order.getProperty());
                    return order.isAscending() ? cb.asc(path) : cb.desc(path);
                })
                .collect(Collectors.toList());
        query.orderBy(orderList);
        var typedQuery = em.createQuery(query);
        var totalItems = typedQuery.getResultList().size();
        // Apply paging
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        var resultList = typedQuery.getResultList();

        return new Pair<>(
                new PageImpl<>(resultList, pageable, totalItems),
                listOutgoings.getResultList()
        );
    }

    private Long parseId(String term) {
        try {
            return Long.parseLong(term);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Predicate searchById(Long id, CriteriaBuilder cb, Root<Outgoing> root) {
        return cb.equal(root.get("id"), id);
    }

    private Predicate searchByDocumentNumber(String term, CriteriaBuilder cb, Root<Outgoing> root) {
        return cb.like(cb.lower(root.get("documentNumber")), "%" + term + "%");
    }

    private Predicate searchByBuilding(String term, CriteriaBuilder cb, Root<Outgoing> root) {
        var buildingJoin = root.join("building");
        return cb.like(cb.lower(buildingJoin.get("name")), "%" + term + "%");
    }

    private Predicate searchBySecret(String term, CriteriaBuilder cb, Root<Outgoing> root) {
        var secretJoin = root.join("secret");
        return cb.like(cb.lower(secretJoin.get("name")), "%" + term + "%");
    }

    private Predicate searchByExecutor(String term, CriteriaBuilder cb, Root<Outgoing> root) {
        var executorJoin = root.join("executor", JoinType.LEFT);
        var executorName = cb.concat(
                cb.concat(cb.literal(" "), executorJoin.get("firstname")),
                executorJoin.get("lastname")
        );
        return cb.like(cb.lower(executorName), "%" + term + "%");
    }

    private Predicate getStatusInPredicate(Set<Status> statuses, CriteriaBuilder cb, Root<Outgoing> root) {
        return root.get("status").in(statuses);
    }

    private void createOutgoingMinDTOQuery(CriteriaBuilder cb, Root<Outgoing> root, CriteriaQuery<OutgoingMinDTO> query) {
        query.multiselect(
                root.get("id").alias("id"),
                root.get("documentNumber").alias("documentNumber"),
                root.get("status").alias("status"),
                root.join("executor", JoinType.LEFT).alias("executor"),
                root.get("createdTimestamp").alias("createdTimestamp"),
                root.get("closedTimestamp").alias("closedTimestamp"),
                root.get("estimatedTimestamp").alias("estimatedTimestamp"),
                root.get("sendingTimestamp").alias("sendingTimestamp"),
                root.get("secret").alias("secret"),
                root.get("building").alias("building")
        );
    }
}
