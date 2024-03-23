package kz.logitex.kartoteka.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import kz.logitex.kartoteka.ingoing.IngoingDTO;
import kz.logitex.kartoteka.ingoing.IngoingMinDTO;
import kz.logitex.kartoteka.model.*;
import kz.logitex.kartoteka.outgoing.OutgoingDTO;
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
        // Add search criteria
        addSearchCriteria(term, cb, root, predicates);

        // Add predicates for filtering by status
        if (statuses != null && !statuses.isEmpty()) {
            predicates.add(getStatusInPredicate(statuses, cb, root));
        }
        // Construct the WHERE clause with the combined predicates
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        // Select the necessary attributes to construct TicketMinDTO
        createOutgoingMinDTOQuery(cb, root, query);
        // Apply sorting
        var orderList = getSortingOrders(pageable, cb, root);
        query.orderBy(orderList);
        var typedQuery = em.createQuery(query);
        var totalItems = typedQuery.getResultList().size();
        // Apply paging
        applyPaging(pageable, typedQuery);
        var resultList = typedQuery.getResultList();

        return new Pair<>(
                new PageImpl<>(resultList, pageable, totalItems),
                listOutgoings.getResultList()
        );
    }

    @Override
    public List<OutgoingDTO> findAllByFilters(Long start, Long end, String description, List<Building> building, List<User> executor) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OutgoingDTO> query = cb.createQuery(OutgoingDTO.class);
        Root<Outgoing> root = query.from(Outgoing.class);

        // Define predicates for filtering
        Predicate predicate = buildPredicate(start, end, description, building, executor, cb, root);

        // Apply predicates to the query
        query.where(predicate);

        // Select the necessary attributes to construct OutgoingDTO
        buildSelection(root, query, cb);

        // Execute the query and convert the results to OutgoingDTO
        return em.createQuery(query).getResultList();
    }

    private Predicate buildPredicate(Long start, Long end, String description, List<Building> building, List<User> executor,
                                     CriteriaBuilder cb, Root<Outgoing> root) {
        Predicate predicate = cb.conjunction();

        // Filter by start and end timestamps
        if (start != null && end != null) {
            predicate = cb.and(predicate, cb.between(root.get("createdTimestamp"), start, end));
        }

        // Filter by building
        if (building != null && !building.isEmpty()) {
            predicate = cb.and(root.get("building").in(building));
        }

        // Filter by description
        if (description != null && !description.isEmpty()) {
            var normalizedDescription = StringModifier.normalizeAndLowerCase(description);
            predicate = cb.and(predicate, cb.like(cb.lower(root.get("description")), "%" + normalizedDescription + "%"));
        }

        if (executor != null && !executor.isEmpty()) {
            predicate = cb.and(root.get("executor").in(executor));
        }

        return predicate;
    }

    private void buildSelection(Root<Outgoing> root, CriteriaQuery<OutgoingDTO> query, CriteriaBuilder cb) {
        query.multiselect(
                root.get("id"),
                root.get("documentNumber"),
                root.get("description"),
                root.get("exemplar"),
                root.get("createdTimestamp"),
                root.get("estimatedTimestamp"),
                root.get("closedTimestamp"),
                root.get("documentTimestamp"),
                root.get("sendingTimestamp"),
                root.get("status"),
                root.get("building"),
                root.get("secret"),
                root.join("executor", JoinType.LEFT).alias("executor"),
                root.get("copyNumber"),
                root.get("copySheet"),
                root.get("sheet"),
                root.get("schedule"),
                root.get("docDepartmentIndex"),
                root.get("docCopySheet"),
                root.get("docCopyPrint"),
                root.get("reregistration"),
                root.get("returnAddress"),
                root.get("onlyAddress")
        );
    }

    private void applyPaging(Pageable pageable, TypedQuery<OutgoingMinDTO> typedQuery) {
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
    }

    private List<Order> getSortingOrders(Pageable pageable, CriteriaBuilder cb, Root<Outgoing> root) {
        return pageable.getSort().stream()
                .map(order -> {
                    var path = root.get(order.getProperty());
                    return order.isAscending() ? cb.asc(path) : cb.desc(path);
                })
                .collect(Collectors.toList());
    }

    private void addSearchCriteria(String term, CriteriaBuilder cb, Root<Outgoing> root, List<Predicate> predicates) {
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
