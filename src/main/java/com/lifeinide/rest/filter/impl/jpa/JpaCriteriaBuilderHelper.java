package com.lifeinide.rest.filter.impl.jpa;

import com.lifeinide.rest.filter.enums.QueryCondition;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

/**
 * @author Lukasz Frankowski
 */
@SuppressWarnings("unchecked")
public class JpaCriteriaBuilderHelper {

	public static final JpaCriteriaBuilderHelper INSTANCE = new JpaCriteriaBuilderHelper();

	protected JpaCriteriaBuilderHelper() {
	}

	public Predicate buildCriteria(QueryCondition condition, CriteriaBuilder criteriaBuilder, Expression<?> x, Object y) {
		switch (condition) {

			case eq:
				return criteriaBuilder.equal(x, y);

			case ne:
				return criteriaBuilder.notEqual(x, y);

			case gt:
				return criteriaBuilder.greaterThan((Expression<Comparable>) x, (Comparable) y);

			case ge:
				return criteriaBuilder.greaterThanOrEqualTo((Expression<Comparable>) x, (Comparable) y);

			case lt:
				return criteriaBuilder.lessThan((Expression<Comparable>) x, (Comparable) y);

			case le:
				return criteriaBuilder.lessThanOrEqualTo((Expression<Comparable>) x, (Comparable) y);

			case isNull:
				return criteriaBuilder.isNull(x);

			case notNull:
				return criteriaBuilder.isNotNull(x);

			default:
				throw new UnsupportedOperationException(String.format("Criterion for: %s is not implemented yet", condition));
		}
	}

}
