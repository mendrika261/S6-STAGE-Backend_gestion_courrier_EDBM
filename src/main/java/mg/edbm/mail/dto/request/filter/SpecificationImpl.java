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
        };
    }

    private Predicate getEqualPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        if (criteria.getValue() == null)
            return builder.isNull(root.get(criteria.getKey()));
        return builder.equal(root.get(criteria.getKey()), criteria.getValue());
    }

    private Predicate getNotEqualPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        if (criteria.getValue() == null)
            return builder.isNotNull(root.get(criteria.getKey()));
        return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
    }

    private Predicate getGreaterThanPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
    }

    private Predicate getLessThanPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
    }

    private Predicate getLikePredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
    }

    private Predicate getNotLikePredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.notLike(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
    }

    private Predicate getGreaterThanOrEqualPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
    }

    private Predicate getLessThanOrEqualPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
    }
}