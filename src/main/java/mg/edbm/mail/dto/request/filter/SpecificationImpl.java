package mg.edbm.mail.dto.request.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SpecificationImpl<T> implements Specification<T> {
    private final List<SearchCriteria> criteriaList = new ArrayList<>();

    public SpecificationImpl(List<SearchCriteria> criteriaList) {
        getCriteriaList().addAll(criteriaList);
    }

    @Override
    public Predicate toPredicate(@NonNull Root<T> root,
                                 @NonNull CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder builder) {
        final List<Predicate> predicates = new ArrayList<>();

        for (final SearchCriteria criteria: getCriteriaList()) {
            predicates.add(getPredicate(root, criteria, builder));
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate getPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return switch (criteria.getOperation()) {
            case EQUAL -> getEqualPredicate(root, criteria, builder);
            case NOT_EQUAL -> getNotEqualPredicate(root, criteria, builder);
            case GREATER_THAN -> getGreaterThanPredicate(root, criteria, builder);
            case LESS_THAN -> getLessThanPredicate(root, criteria, builder);
            case LIKE -> getLikePredicate(root, criteria, builder);
            case NOT_LIKE -> getNotLikePredicate(root, criteria, builder);
            case GREATER_THAN_OR_EQUAL -> getGreaterThanOrEqualPredicate(root, criteria, builder);
            case LESS_THAN_OR_EQUAL -> getLessThanOrEqualPredicate(root, criteria, builder);
            case STARTS_WITH -> getStartsWithPredicate(root, criteria, builder);
            case ENDS_WITH -> getEndsWithPredicate(root, criteria, builder);
            case IS_NULL -> getIsNullPredicate(root, criteria, builder);
            case IS_NOT_NULL -> getIsNotNullPredicate(root, criteria, builder);
        };
    }

    private Predicate getEqualPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        if (criteria.getValue() == null)
            return builder.isNull(root.get(criteria.getKey()));
        return builder.equal(builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase());
    }

    private Predicate getNotEqualPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        if (criteria.getValue() == null)
            return builder.isNotNull(root.get(criteria.getKey()));
        return builder.notEqual(builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase());
    }

    private Predicate getGreaterThanPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.greaterThan(builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase());
    }

    private Predicate getLessThanPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.lessThan(builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase());
    }

    private Predicate getLikePredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
    }

    private Predicate getNotLikePredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.notLike(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
    }

    private Predicate getGreaterThanOrEqualPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.greaterThanOrEqualTo(builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase());
    }

    private Predicate getLessThanOrEqualPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.lessThanOrEqualTo(builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase());
    }

    private Predicate getStartsWithPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.like(builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase() + "%");
    }

    private Predicate getEndsWithPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase());
    }

    private Predicate getIsNullPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.isNull(root.get(criteria.getKey()));
    }

    private Predicate getIsNotNullPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.isNotNull(root.get(criteria.getKey()));
    }
}