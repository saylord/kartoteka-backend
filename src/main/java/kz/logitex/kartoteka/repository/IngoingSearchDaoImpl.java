package kz.logitex.kartoteka.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import kz.logitex.kartoteka.ingoing.IngoingDTO;
import kz.logitex.kartoteka.ingoing.IngoingMinDTO;
import kz.logitex.kartoteka.model.Building;
import kz.logitex.kartoteka.model.Ingoing;
import kz.logitex.kartoteka.model.Status;
import kz.logitex.kartoteka.model.User;
import kz.logitex.kartoteka.util.StringModifier;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Calendar;
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
                                                                         int year,
                                                                         Pageable pageable) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(IngoingMinDTO.class);
        var root = query.from(Ingoing.class);

        // Create a list to hold the predicates for filtering
        var predicates = new ArrayList<Predicate>();
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        createIngoingMinDTOQuery(cb, root, query);

        var listIngoings = em.createQuery(query);
        // Add search criteria
        addSearchCriteria(term, year, cb, root, predicates);

        // Add predicates for filtering by status
        if (statuses != null && !statuses.isEmpty()) {
            predicates.add(getStatusInPredicate(statuses, cb, root));
        }
        // Construct the WHERE clause with the combined predicates
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        // Select the necessary attributes to construct IngoingMinDTO
        createIngoingMinDTOQuery(cb, root, query);
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
                listIngoings.getResultList()
        );
    }

    @Override
    public List<IngoingDTO> findAllByFilters(Long start, Long end, String description, List<Building> building) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IngoingDTO> query = cb.createQuery(IngoingDTO.class);
        Root<Ingoing> root = query.from(Ingoing.class);

        // Define predicates for filtering
        Predicate predicate = buildPredicate(start, end, description, building, cb, root);

        // Apply predicates to the query
        query.where(predicate);

        // Select the necessary attributes to construct IngoingDTO
        buildSelection(root, query, cb);

        // Execute the query and convert the results to IngoingDTO
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<Ingoing> search(String term) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(Ingoing.class);
        var root = query.from(Ingoing.class);

        var predicates = new ArrayList<Predicate>();
        addSearchCriteria(term, 0, cb, root, predicates);
        query.where(cb.or(predicates.toArray(new Predicate[0])));
        return em.createQuery(query).getResultList();
    }

    private Predicate buildPredicate(Long start, Long end, String description, List<Building> building,
                                     CriteriaBuilder cb, Root<Ingoing> root) {
        Predicate predicate = cb.conjunction();

        // Filter by start and end timestamps
        if (start != null && end != null) {
            predicate = cb.and(predicate, cb.between(root.get("createdTimestamp"), start, end));
        }

        // Filter by building
        if (building != null && !building.isEmpty()) {
            predicate = cb.and(predicate, root.get("building").in(building));
        }

        // Filter by description
        if (description != null && !description.isEmpty()) {
            var normalizedDescription = StringModifier.normalizeAndLowerCase(description);
            predicate = cb.and(predicate, cb.like(cb.lower(root.get("description")), "%" + normalizedDescription + "%"));
        }

        return predicate;
    }

    private void buildSelection(Root<Ingoing> root, CriteriaQuery<IngoingDTO> query, CriteriaBuilder cb) {
        query.multiselect(
                root.get("id"),
                root.get("documentNumber"),
                root.get("cardNumber"),
                root.get("description"),
                root.get("resolution"),
                root.get("createdTimestamp"),
                root.get("estimatedTimestamp"),
                root.get("closedTimestamp"),
                root.get("documentTimestamp"),
                root.join("executor", JoinType.LEFT).alias("executor"),
                root.get("status"),
                root.get("building"),
                root.get("secret"),
                root.get("exemplar"),
                root.get("totalSheet"),
                root.get("sheet"),
                root.get("schedule"),
                root.get("reregistrationTimestamp"),
                root.get("caseNumber"),
                root.get("nomenclature"),
                root.get("annualTimestamp"),
                root.get("semiAnnualTimestamp"),
                root.get("monthlyTimestamp"),
                root.get("tenDayTimestamp"),
                root.get("annual"),
                root.get("semiAnnual"),
                root.get("monthly"),
                root.get("tenDay")
        );
    }

    private void addSearchCriteria(String term, int year, CriteriaBuilder cb, Root<Ingoing> root, List<Predicate> predicates) {
        if (term != null && !term.isEmpty()) {
            var id = parseId(term);
            if (id != null) {
                var searchConditions = new ArrayList<Predicate>();
                searchConditions.add(searchById(id, cb, root));
                searchConditions.add(searchByDocumentNumber(term, cb, root));
                var searchCondition = cb.or(searchConditions.toArray(new Predicate[0]));
                predicates.add(searchCondition);
            } else {
                var normalizedTerm = StringModifier.normalizeAndLowerCase(term);
                var searchConditions = new ArrayList<Predicate>();
                searchConditions.add(searchByDocumentNumber(normalizedTerm, cb, root));
                searchConditions.add(searchByBuilding(normalizedTerm, cb, root));
                searchConditions.add(searchBySecret(normalizedTerm, cb, root));
                searchConditions.add(searchByCardNumber(normalizedTerm, cb, root));
                searchConditions.add(searchByExecutor(normalizedTerm, cb, root));
                searchConditions.add(searchByDescription(normalizedTerm, cb, root));
                var searchCondition = cb.or(searchConditions.toArray(new Predicate[0]));
                predicates.add(searchCondition);
            }
        }

        if (year > 1970) {
            long startOfYear = getStartOfYearTimestamp(year);
            long endOfYear = getEndOfYearTimestamp(year);

            predicates.add(cb.greaterThanOrEqualTo(root.get("createdTimestamp"), startOfYear));
            predicates.add(cb.lessThan(root.get("createdTimestamp"), endOfYear));
        }
    }

    private long getStartOfYearTimestamp(int year) {
        var calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTimeInMillis();
    }

    private long getEndOfYearTimestamp(int year) {
        var calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year + 1);
        return calendar.getTimeInMillis();
    }

    private void applyPaging(Pageable pageable, TypedQuery<IngoingMinDTO> typedQuery) {
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
    }

    private List<Order> getSortingOrders(Pageable pageable, CriteriaBuilder cb, Root<Ingoing> root) {
        return pageable.getSort().stream()
                .map(order -> {
                    var path = root.get(order.getProperty());
                    return order.isAscending() ? cb.asc(path) : cb.desc(path);
                })
                .collect(Collectors.toList());
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

    private Predicate searchByCardNumber(String term, CriteriaBuilder cb, Root<Ingoing> root) {
        return cb.like(cb.lower(root.get("cardNumber")), "%" + term + "%");
    }

    private Predicate searchByExecutor(String term, CriteriaBuilder cb, Root<Ingoing> root) {
        var executorJoin = root.join("executor", JoinType.LEFT);
        var executorName = cb.concat(
                cb.concat(cb.literal(" "), executorJoin.get("firstname")),
                executorJoin.get("lastname")
        );
        return cb.like(cb.lower(executorName), "%" + term + "%");
    }

    private Predicate searchByDescription(String term, CriteriaBuilder cb, Root<Ingoing> root) {
        return cb.like(cb.lower(root.get("description")), "%" + term + "%");
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
                root.get("cardNumber").alias("cardNumber"),
                root.get("description").alias("description"),
                root.get("status").alias("status"),
                root.get("createdTimestamp").alias("createdTimestamp"),
                root.get("closedTimestamp").alias("closedTimestamp"),
                root.get("estimatedTimestamp").alias("estimatedTimestamp"),
                root.join("executor", JoinType.LEFT).alias("executor"),
                root.get("building").alias("building"),
                root.get("secret").alias("secret"),
                root.get("annualTimestamp").alias("annualTimestamp"),
                root.get("semiAnnualTimestamp").alias("semiAnnualTimestamp"),
                root.get("monthlyTimestamp").alias("monthlyTimestamp"),
                root.get("tenDayTimestamp").alias("tenDayTimestamp"),
                root.get("annual").alias("annual"),
                root.get("semiAnnual").alias("semiAnnual"),
                root.get("monthly").alias("monthly"),
                root.get("tenDay").alias("tenDay")
        );
    }
}

