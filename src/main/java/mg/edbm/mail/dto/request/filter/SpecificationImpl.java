package mg.edbm.mail.dto.request.filter;

import jakarta.persistence.Entity;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.type.LogicOperationType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SpecificationImpl<T> implements Specification<T> {
    private ListRequest listRequest;

    public SpecificationImpl(ListRequest listRequest) {
        setListRequest(listRequest);
    }


    @Override
    public Predicate toPredicate(@NonNull Root<T> root,
                                 @NonNull CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder builder) {
        Predicate predicate = builder.conjunction();
        Predicate basePredicate = builder.conjunction();

        for (final SearchCriteria criteria: getListRequest().getBaseCriteria()) {
            basePredicate = builder.and(basePredicate, getPredicate(root, criteria, builder));
        }

        for (final SearchCriteria criteria: getListRequest().getCriteria()) {
            if(criteria.getLogicOperationType().equals(LogicOperationType.OR))
                predicate = builder.or(predicate, getPredicate(root, criteria, builder));
            else
                predicate = builder.and(predicate, getPredicate(root, criteria, builder));
        }

        return builder.and(basePredicate, predicate);
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

    private <Y> Path<Y> getPath(Root<T> root, String key) {
        final String[] keys = key.split("\\.");
        Path<Y> path = root.get(keys[0]);
        for (int i = 1; i < keys.length; i++)
            path = path.get(keys[i]);
        return path;
    }

    private Object castType(Class<?> type, Object value) {
        if(type == UUID.class)
            return UUID.fromString(value.toString());
        if(type == Integer.class)
            return Integer.parseInt(value.toString());
        if(type == Long.class)
            return Long.parseLong(value.toString());
        if(type == Double.class)
            return Double.parseDouble(value.toString());
        if(type == Float.class)
            return Float.parseFloat(value.toString());
        if(type == Boolean.class)
            return Boolean.parseBoolean(value.toString());
        return value.toString().toLowerCase();
    }

    private Predicate getEqualPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        final Path<?> current = getPath(root, criteria.getKey());
        if(criteria.getValue() == null)
            return builder.isNull(current);
        if(current.getJavaType() == LocalDateTime.class || current.getJavaType() == LocalDate.class)
            return getStartsWithPredicate(root, criteria, builder);
        if(current.getJavaType() == String.class)
            return builder.equal(builder.lower((Expression<String>) current), castType(String.class, criteria.getValue()));
        if(current.getJavaType().isEnum())
            return current.in(criteria.getValue());
        if(current.getJavaType().isAnnotationPresent(Entity.class))
            return builder.equal(current.get("id"), criteria.getValue());
        return builder.equal(current, castType(current.getJavaType(), criteria.getValue()));
    }

    private Predicate getNotEqualPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        final Path<?> current = getPath(root, criteria.getKey());
        if(criteria.getValue() == null)
            return builder.isNotNull(current);
        if(current.getJavaType() == LocalDateTime.class || current.getJavaType() == LocalDate.class)
            return builder.not(getStartsWithPredicate(root, criteria, builder));
        if(current.getJavaType() == String.class)
            return builder.notEqual(builder.lower((Expression<String>) current), criteria.getValue().toString().toLowerCase());
        if(current.getJavaType().isEnum())
            return builder.not(current.in(criteria.getValue()));
        if(current.getJavaType().isAnnotationPresent(Entity.class))
            return builder.not(current.get("id").in(criteria.getValue()));
        return builder.notEqual(current, castType(current.getJavaType(), criteria.getValue()));
    }

    private Predicate getGreaterThanPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.greaterThan(
                builder.lower(getPath(root, criteria.getKey()).as(String.class)),
                criteria.getValue().toString().toLowerCase()
        );
    }

    private Predicate getLessThanPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.lessThan(
                builder.lower(getPath(root, criteria.getKey()).as(String.class)),
                criteria.getValue().toString().toLowerCase()
        );
    }

    private Predicate getLikePredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        final String value = "%"+criteria.getValue().toString().replaceAll("[\\p{Punct}\\s]", "%").toLowerCase()+"%";
        return builder.like(builder.lower(getPath(root, criteria.getKey()).as(String.class)), value);
    }

    private Predicate getNotLikePredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        final String value = "%"+criteria.getValue().toString().replaceAll("[\\p{Punct}\\s]", "%").toLowerCase()+"%";
        return builder.notLike(builder.lower(getPath(root, criteria.getKey()).as(String.class)), value);
    }

    private Predicate getGreaterThanOrEqualPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.greaterThanOrEqualTo(
                builder.lower(getPath(root, criteria.getKey()).as(String.class)),
                criteria.getValue().toString().toLowerCase()
        );
    }

    private Predicate getLessThanOrEqualPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.lessThanOrEqualTo(
                builder.lower(getPath(root, criteria.getKey()).as(String.class)),
                criteria.getValue().toString().toLowerCase()
        );
    }

    private Predicate getStartsWithPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.like(
                builder.lower(getPath(root, criteria.getKey()).as(String.class)),
                criteria.getValue().toString().toLowerCase() + "%"
        );
    }

    private Predicate getEndsWithPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.like(
                builder.lower(getPath(root, criteria.getKey()).as(String.class)),
                "%" + criteria.getValue().toString().toLowerCase()
        );
    }

    private Predicate getIsNullPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.isNull(getPath(root, criteria.getKey()));
    }

    private Predicate getIsNotNullPredicate(Root<T> root, SearchCriteria criteria, CriteriaBuilder builder) {
        return builder.isNotNull(getPath(root, criteria.getKey()));
    }
}