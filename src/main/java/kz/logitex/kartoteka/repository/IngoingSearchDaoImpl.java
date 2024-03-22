package kz.logitex.kartoteka.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kz.logitex.kartoteka.ingoing.IngoingMinDTO;
import kz.logitex.kartoteka.model.Ingoing;
import kz.logitex.kartoteka.model.Status;
import kz.logitex.kartoteka.util.AuthUtil;
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
public class IngoingSearchDaoImpl implements IngoingSearchDao {
    private final EntityManager em;

    @Override
    public Pair<Page<IngoingMinDTO>, List<IngoingMinDTO>> findByStatusIn(String term,
                                                                         Set<Status> statuses,
                                                                         Pageable pageable) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(IngoingMinDTO.class);
        var root = query.from(Ingoing.class);

        // Create a list to hold the predicates for filtering
        var predicates = new ArrayList<Predicate>();
        System.out.println("111");
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        createIngoingMinDTOQuery(cb, root, query);
        var listIngoings = em.createQuery(query);
        System.out.println("222");
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
        createIngoingMinDTOQuery(cb, root, query);

        // Apply sorting
        var orderList = pageable.getSort().stream()
                .map(order -> {
                    var path = root.get(order.getProperty());
                    return order.isAscending() ? cb.asc(path) : cb.desc(path);
                })
                .collect(Collectors.toList());
        query.orderBy(orderList);
        System.out.println("333");
        var typedQuery = em.createQuery(query);
        var totalItems = typedQuery.getResultList().size();

        // Apply paging
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        var resultList = typedQuery.getResultList();
        System.out.println("555");

        return new Pair<>(
                new PageImpl<>(resultList, pageable, totalItems),
                listIngoings.getResultList()
        );
    }

    private Long parseId(String term) {
        try {
            return Long.parseLong(term);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Predicate searchById(Long id, CriteriaBuilder cb, Root<Ingoing> root) {
        return cb.equal(root.get("id"), id);
    }

    private Predicate searchByDocumentNumber(String term, CriteriaBuilder cb, Root<Ingoing> root) {
        return cb.like(cb.lower(root.get("documentNumber")), "%" + term + "%");
    }

    private Predicate searchByBuilding(String term, CriteriaBuilder cb, Root<Ingoing> root) {
        var buildingJoin = root.join("building");
        return cb.like(cb.lower(buildingJoin.get("name")), "%" + term + "%");
    }

    private Predicate searchBySecret(String term, CriteriaBuilder cb, Root<Ingoing> root) {
        var secretJoin = root.join("secret");
        return cb.like(cb.lower(secretJoin.get("name")), "%" + term + "%");
    }

    private Predicate getStatusInPredicate(Set<Status> statuses, CriteriaBuilder cb, Root<Ingoing> root) {
        return root.get("status").in(statuses);
    }

    private void createIngoingMinDTOQuery(CriteriaBuilder cb, Root<Ingoing> root, CriteriaQuery<IngoingMinDTO> query) {
        query.multiselect(
                root.get("id").alias("id"),
                root.get("documentNumber").alias("documentNumber"),
                root.get("status").alias("status"),
                root.get("createdTimestamp").alias("createdTimestamp"),
                root.get("closedTimestamp").alias("closedTimestamp"),
                root.get("estimatedTimestamp").alias("estimatedTimestamp"),
                root.get("secret").alias("secret"),
                root.get("building").alias("building")
        );
    }
}

